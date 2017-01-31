package mir.reactions.reactionsJavaTo5_1.ejbjava2pcm;

import mir.routines.ejbjava2pcm.RoutinesFacade;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.commons.NamedElement;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.palladiosimulator.pcm.repository.Repository;
import tools.vitruv.applications.pcmjava.ejbtransformations.java2pcm.EJBAnnotationHelper;
import tools.vitruv.applications.pcmjava.ejbtransformations.java2pcm.EJBJava2PcmHelper;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractReactionRealization;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;
import tools.vitruv.framework.change.echange.EChange;
import tools.vitruv.framework.change.echange.compound.CreateAndInsertNonRoot;
import tools.vitruv.framework.change.echange.feature.reference.InsertEReference;
import tools.vitruv.framework.userinteraction.UserInteracting;

@SuppressWarnings("all")
class CreateInterfaceAnnotationReaction extends AbstractReactionRealization {
  public CreateInterfaceAnnotationReaction(final UserInteracting userInteracting) {
    super(userInteracting);
  }
  
  public void executeReaction(final EChange change) {
    InsertEReference<Interface, AnnotationInstanceOrModifier> typedChange = ((CreateAndInsertNonRoot<Interface, AnnotationInstanceOrModifier>)change).getInsertChange();
    Interface affectedEObject = typedChange.getAffectedEObject();
    EReference affectedFeature = typedChange.getAffectedFeature();
    AnnotationInstanceOrModifier newValue = typedChange.getNewValue();
    mir.routines.ejbjava2pcm.RoutinesFacade routinesFacade = new mir.routines.ejbjava2pcm.RoutinesFacade(this.executionState, this);
    mir.reactions.reactionsJavaTo5_1.ejbjava2pcm.CreateInterfaceAnnotationReaction.ActionUserExecution userExecution = new mir.reactions.reactionsJavaTo5_1.ejbjava2pcm.CreateInterfaceAnnotationReaction.ActionUserExecution(this.executionState, this);
    userExecution.callRoutine1(affectedEObject, affectedFeature, newValue, routinesFacade);
  }
  
  public static Class<? extends EChange> getExpectedChangeType() {
    return CreateAndInsertNonRoot.class;
  }
  
  private boolean checkChangeProperties(final EChange change) {
    InsertEReference<Interface, AnnotationInstanceOrModifier> relevantChange = ((CreateAndInsertNonRoot<Interface, AnnotationInstanceOrModifier>)change).getInsertChange();
    // Check affected object
    if (!(relevantChange.getAffectedEObject() instanceof Interface)) {
    	return false;
    }
    // Check feature
    if (!relevantChange.getAffectedFeature().getName().equals("annotationsAndModifiers")) {
    	return false;
    }
    if (!(relevantChange.getNewValue() instanceof AnnotationInstanceOrModifier)) {
    	return false;
    }
    return true;
  }
  
  public boolean checkPrecondition(final EChange change) {
    if (!(change instanceof CreateAndInsertNonRoot)) {
    	return false;
    }
    if (!checkChangeProperties(change)) {
    	return false;
    }
    InsertEReference<Interface, AnnotationInstanceOrModifier> typedChange = ((CreateAndInsertNonRoot<Interface, AnnotationInstanceOrModifier>)change).getInsertChange();
    Interface affectedEObject = typedChange.getAffectedEObject();
    EReference affectedFeature = typedChange.getAffectedFeature();
    AnnotationInstanceOrModifier newValue = typedChange.getNewValue();
    if (!checkUserDefinedPrecondition(affectedEObject, affectedFeature, newValue)) {
    	return false;
    }
    getLogger().debug("Passed precondition check of reaction " + this.getClass().getName());
    return true;
  }
  
  private boolean checkUserDefinedPrecondition(final Interface affectedEObject, final EReference affectedFeature, final AnnotationInstanceOrModifier newValue) {
    boolean _isEJBBuisnessInterface = EJBAnnotationHelper.isEJBBuisnessInterface(affectedEObject);
    return _isEJBBuisnessInterface;
  }
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public void callRoutine1(final Interface affectedEObject, final EReference affectedFeature, final AnnotationInstanceOrModifier newValue, @Extension final RoutinesFacade _routinesFacade) {
      final Repository repo = EJBJava2PcmHelper.findRepository(this.correspondenceModel);
      _routinesFacade.createOperationInterface(repo, ((NamedElement) affectedEObject));
    }
  }
}
