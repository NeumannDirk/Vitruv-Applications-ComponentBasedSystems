package tools.vitruv.applications.pcmjava.util.java2pcm

import java.util.ArrayList
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.Set
import org.apache.log4j.Logger
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.emftext.language.java.classifiers.Classifier
import org.emftext.language.java.classifiers.ClassifiersFactory
import org.emftext.language.java.classifiers.ConcreteClassifier
import org.emftext.language.java.types.ClassifierReference
import org.emftext.language.java.types.NamespaceClassifierReference
import org.emftext.language.java.types.PrimitiveType
import org.emftext.language.java.types.Type
import org.emftext.language.java.types.TypeReference
import org.emftext.language.java.types.TypedElement
import org.palladiosimulator.pcm.repository.OperationInterface
import org.palladiosimulator.pcm.repository.OperationProvidedRole
import org.palladiosimulator.pcm.repository.OperationRequiredRole
import org.palladiosimulator.pcm.repository.Repository
import org.palladiosimulator.pcm.repository.RepositoryComponent
import org.palladiosimulator.pcm.repository.RepositoryFactory
import org.palladiosimulator.pcm.system.System

import static extension tools.vitruv.framework.correspondence.CorrespondenceModelUtil.*
import org.eclipse.emf.ecore.util.EcoreUtil
import tools.vitruv.framework.correspondence.CorrespondenceModel
import tools.vitruv.framework.util.command.ResourceAccess
import tools.vitruv.domains.pcm.PcmNamespace
import tools.vitruv.domains.java.JavaNamespace
import tools.vitruv.applications.pcmjava.util.PcmJavaUtils
import tools.vitruv.framework.userinteraction.UserInteractionOptions.WindowModality
import tools.vitruv.framework.userinteraction.UserInteractor

abstract class Java2PcmUtils extends PcmJavaUtils {
	private new() {
	}

	private static Logger logger = Logger.getLogger(Java2PcmUtils.simpleName)

	def public static Repository getRepository(CorrespondenceModel correspondenceModel) {
		val Set<Repository> repos = correspondenceModel.getAllEObjectsOfTypeInCorrespondences(Repository)
		if (repos.nullOrEmpty) {
			return null
		}
		if (1 != repos.size) {
			logger.warn("found more than one repository. Retruning the first")
		}
		return repos.get(0)
	}

	def static addJaMoPP2PCMCorrespondenceToFeatureCorrespondenceMap(String jaMoPPFeatureName, String pcmFeatureName,
		Map<EStructuralFeature, EStructuralFeature> featureCorrespondenceMap) {
		var nameAttribute = ClassifiersFactory.eINSTANCE.createInterface.eClass.getEAllAttributes.filter [ attribute |
			attribute.name.equalsIgnoreCase(jaMoPPFeatureName)
		].iterator.next
		var entityNameAttribute = RepositoryFactory.eINSTANCE.createOperationInterface.eClass.getEAllAttributes.filter [ attribute |
			attribute.name.equalsIgnoreCase(pcmFeatureName)
		].iterator.next
		featureCorrespondenceMap.put(nameAttribute, entityNameAttribute)
	}

	def static addName2EntityNameCorrespondence(Map<EStructuralFeature, EStructuralFeature> featureCorrespondenceMap) {
		addJaMoPP2PCMCorrespondenceToFeatureCorrespondenceMap(JavaNamespace.JAMOPP_ATTRIBUTE_NAME,
			PcmNamespace.PCM_ATTRIBUTE_ENTITY_NAME, featureCorrespondenceMap)
	}

	def static createNewCorrespondingEObjects(EObject newEObject, EObject[] newCorrespondingEObjects,
		CorrespondenceModel correspondenceModel, ResourceAccess resourceAccess) {
		if (newCorrespondingEObjects.nullOrEmpty) {
			return
		}
		for (pcmElement : newCorrespondingEObjects) {
			if (pcmElement instanceof Repository || pcmElement instanceof System) {
				PcmJavaUtils.handleRootChange(pcmElement, correspondenceModel,
					PcmJavaUtils.getSourceModelVURI(newEObject), resourceAccess)
			} else {
				// do nothing. save will be done later
			}
			correspondenceModel.createAndAddCorrespondence(pcmElement, newEObject)
		}
	}
	
	def dispatch static Classifier getTargetClassifierFromTypeReference(TypeReference reference) {
		return null
	}
	
	def dispatch static Classifier getTargetClassifierFromTypeReference(NamespaceClassifierReference reference) {
		if (reference.classifierReferences.nullOrEmpty) {
			return null
		}
		return getTargetClassifierFromTypeReference(reference.classifierReferences.get(0))
	}
	
	def dispatch static Classifier getTargetClassifierFromTypeReference(ClassifierReference reference) {
		return reference.target
	}
	
	def dispatch static Classifier getTargetClassifierFromTypeReference(PrimitiveType reference) {
		return null
	}
	
	/**
	 * Try to automatically find the corresponding repository component for a given classifier by 
	 * a) looking for a direct component
	 * a2) looking for a direct corresponding System-->it has no corresponding component in that case
	 * b) looking for the a component class in the same package
	 * c) asking the user in which component the classifier is
	 * @param classifier the classifier
	 */
	def static RepositoryComponent getComponentOfConcreteClassifier(ConcreteClassifier classifier,
		CorrespondenceModel ci, UserInteractor userInteractor) {
	
		// a)
		var correspondingComponents = ci.getCorrespondingEObjectsByType(classifier, RepositoryComponent)
		if (!correspondingComponents.nullOrEmpty) {
			return correspondingComponents.get(0)
		}
	
		// a2)
		var correspondingComposedProvidingRequiringEntitys = ci.getCorrespondingEObjectsByType(classifier, System)
		if (!correspondingComposedProvidingRequiringEntitys.nullOrEmpty) {
			return null
		}
	
		// b)
		for (Classifier classifierInSamePackage : classifier.containingCompilationUnit.classifiersInSamePackage) {
			correspondingComponents = ci.getCorrespondingEObjectsByType(classifierInSamePackage,
				RepositoryComponent)
			if (!correspondingComponents.nullOrEmpty) {
				return correspondingComponents.get(0)
			}
		}
	
		// c)
		val repo = getRepository(ci)
		val String msg = "Please specify the component for class: " +
			classifier.containingCompilationUnit.namespacesAsString + classifier.name
		val List<String> selections = new ArrayList<String>
		repo.components__Repository.forEach[comp|selections.add(comp.entityName)]
		selections.add("Class is not in any component")
		val int selection = userInteractor.singleSelectionDialogBuilder.message(msg).choices(selections)
		    .windowModality(WindowModality.MODAL).startInteraction()
		if (selection == selections.size) {
			return null
		}
		return repo.components__Repository.get(selection)
	}
	
	def public static EObject[] checkAndAddOperationRequiredRole(TypedElement typedElement,
		CorrespondenceModel correspondenceModel, UserInteractor userInteractor) {
		val Type type = getTargetClassifierFromImplementsReferenceAndNormalizeURI(typedElement.typeReference)
		if (null === type) {
			return null
		}
		val Set<EObject> newCorrespondingEObjects = new HashSet
		val fieldTypeCorrespondences = correspondenceModel.getCorrespondingEObjects(type)
		val correspondingInterfaces = fieldTypeCorrespondences.filter(typeof(OperationInterface))
		var RepositoryComponent repoComponent = null
		if (!correspondingInterfaces.nullOrEmpty) {
			for (correspondingInterface : correspondingInterfaces) {
	
				// ii)a)
				repoComponent = Java2PcmUtils.getComponentOfConcreteClassifier(
					typedElement.containingConcreteClassifier, correspondenceModel, userInteractor)
				if (null === repoComponent) {
					return null
				}
				val OperationRequiredRole operationRequiredRole = RepositoryFactory.eINSTANCE.
					createOperationRequiredRole
				operationRequiredRole.requiredInterface__OperationRequiredRole = correspondingInterface
				operationRequiredRole.requiringEntity_RequiredRole = repoComponent
				operationRequiredRole.entityName = "Component_" + repoComponent.entityName + "_requires_" +
					correspondingInterface.entityName
				newCorrespondingEObjects.add(operationRequiredRole)
				userInteractor.notificationDialogBuilder.message("An OperationRequiredRole (from component "
				    + repoComponent.entityName + " to interface " + correspondingInterface.entityName
				    + ") for the element: " + typedElement + " has been created.")
				    .windowModality(WindowModality.MODELESS).startInteraction()
					}
				}
	
				val correspondingComponents = fieldTypeCorrespondences.filter(typeof(RepositoryComponent))
				if (!correspondingComponents.nullOrEmpty) {
					if (null === repoComponent) {
						repoComponent = Java2PcmUtils.getComponentOfConcreteClassifier(
							typedElement.containingConcreteClassifier, correspondenceModel, userInteractor)
					}
					if (null === repoComponent) {
						return null
					}
	
					// ii)b)
					for (correspondingComponent : correspondingComponents) {
						for (OperationProvidedRole operationProvidedRole : correspondingComponent.
							providedRoles_InterfaceProvidingEntity.filter(typeof(OperationProvidedRole))) {
							var operationInterface = operationProvidedRole.providedInterface__OperationProvidedRole
							val OperationRequiredRole operationRequiredRole = RepositoryFactory.eINSTANCE.
								createOperationRequiredRole
							operationRequiredRole.requiredInterface__OperationRequiredRole = operationInterface
							operationRequiredRole.requiringEntity_RequiredRole = repoComponent
							operationRequiredRole.entityName = "Component_" + repoComponent.entityName +
								"_requires_" + operationInterface.entityName
							userInteractor.notificationDialogBuilder.message("An OperationRequiredRole (from component "
							    + repoComponent.entityName + " to interface " + operationInterface.entityName
							    + ") for the element: " + typedElement + " has been created.")
							    .windowModality(WindowModality.MODELESS).startInteraction()
								
							newCorrespondingEObjects.add(operationRequiredRole)
						}
					}
				}
				return newCorrespondingEObjects
			}
	
	def public static Classifier getTargetClassifierFromImplementsReferenceAndNormalizeURI(
		TypeReference reference) {
		var interfaceClassifier = Java2PcmUtils.getTargetClassifierFromTypeReference(reference)
		if (null === interfaceClassifier) {
			return null
		}
		
		if(interfaceClassifier.eIsProxy){
			val resSet = reference.eResource.resourceSet
			interfaceClassifier = EcoreUtil.resolve(interfaceClassifier, resSet) as Classifier			
		}
		normalizeURI(interfaceClassifier)
		return interfaceClassifier
	}

	def public static normalizeURI(EObject eObject) {
		if (null === eObject.eResource || null === eObject.eResource.resourceSet) {
			return false
		}
		val resource = eObject.eResource
		val resourceSet = resource.resourceSet
		val uri = resource.getURI
		val uriConverter = resourceSet.getURIConverter
		val normalizedURI = uriConverter.normalize(uri)
		resource.URI = normalizedURI
		return true
	}

}
