package mir.routines.pcm2java;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.HashSet;
import java.util.function.Consumer;
import mir.routines.pcm2java.RoutinesFacade;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.emftext.language.java.members.InterfaceMethod;
import org.palladiosimulator.pcm.core.entity.InterfaceProvidingEntity;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.seff.ServiceEffectSpecification;
import tools.vitruv.extensions.dslsruntime.response.AbstractEffectRealization;
import tools.vitruv.extensions.dslsruntime.response.ResponseExecutionState;
import tools.vitruv.extensions.dslsruntime.response.structure.CallHierarchyHaving;

@SuppressWarnings("all")
public class RenameMethodForOperationSignatureEffect extends AbstractEffectRealization {
  public RenameMethodForOperationSignatureEffect(final ResponseExecutionState responseExecutionState, final CallHierarchyHaving calledBy, final OperationSignature operationSignature) {
    super(responseExecutionState, calledBy);
    				this.operationSignature = operationSignature;
  }
  
  private OperationSignature operationSignature;
  
  private static class EffectUserExecution extends AbstractEffectRealization.UserExecution {
    public EffectUserExecution(final ResponseExecutionState responseExecutionState, final CallHierarchyHaving calledBy) {
      super(responseExecutionState);
    }
    
    private void executeUserOperations(final OperationSignature operationSignature, final InterfaceMethod interfaceMethod) {
      String _entityName = operationSignature.getEntityName();
      interfaceMethod.setName(_entityName);
    }
  }
  
  private static class CallRoutinesUserExecution extends AbstractEffectRealization.UserExecution {
    @Extension
    private RoutinesFacade effectFacade;
    
    public CallRoutinesUserExecution(final ResponseExecutionState responseExecutionState, final CallHierarchyHaving calledBy) {
      super(responseExecutionState);
      this.effectFacade = new mir.routines.pcm2java.RoutinesFacade(responseExecutionState, calledBy);
    }
    
    private void executeUserOperations(final OperationSignature operationSignature, final InterfaceMethod interfaceMethod) {
      final OperationInterface operationInterface = operationSignature.getInterface__OperationSignature();
      final HashSet<InterfaceProvidingEntity> implementingComponents = Sets.<InterfaceProvidingEntity>newHashSet();
      Repository _repository__Interface = operationInterface.getRepository__Interface();
      EList<RepositoryComponent> _components__Repository = _repository__Interface.getComponents__Repository();
      final Consumer<RepositoryComponent> _function = (RepositoryComponent comp) -> {
        EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = comp.getProvidedRoles_InterfaceProvidingEntity();
        final Iterable<OperationProvidedRole> opProvRoles = Iterables.<OperationProvidedRole>filter(_providedRoles_InterfaceProvidingEntity, OperationProvidedRole.class);
        final Function1<OperationProvidedRole, Boolean> _function_1 = (OperationProvidedRole it) -> {
          OperationInterface _providedInterface__OperationProvidedRole = it.getProvidedInterface__OperationProvidedRole();
          String _id = _providedInterface__OperationProvidedRole.getId();
          String _id_1 = operationInterface.getId();
          return Boolean.valueOf(Objects.equal(_id, _id_1));
        };
        Iterable<OperationProvidedRole> _filter = IterableExtensions.<OperationProvidedRole>filter(opProvRoles, _function_1);
        final Consumer<OperationProvidedRole> _function_2 = (OperationProvidedRole opProRole) -> {
          InterfaceProvidingEntity _providingEntity_ProvidedRole = opProRole.getProvidingEntity_ProvidedRole();
          implementingComponents.add(_providingEntity_ProvidedRole);
        };
        _filter.forEach(_function_2);
      };
      _components__Repository.forEach(_function);
      final Iterable<BasicComponent> basicComponents = Iterables.<BasicComponent>filter(implementingComponents, BasicComponent.class);
      final Consumer<BasicComponent> _function_1 = (BasicComponent it) -> {
        EList<ServiceEffectSpecification> _serviceEffectSpecifications__BasicComponent = it.getServiceEffectSpecifications__BasicComponent();
        final Consumer<ServiceEffectSpecification> _function_2 = (ServiceEffectSpecification it_1) -> {
          this.effectFacade.updateSEFFImplementingMethodName(it_1);
        };
        _serviceEffectSpecifications__BasicComponent.forEach(_function_2);
      };
      basicComponents.forEach(_function_1);
    }
  }
  
  private EObject getCorrepondenceSourceInterfaceMethod(final OperationSignature operationSignature) {
    return operationSignature;
  }
  
  protected void executeRoutine() throws IOException {
    getLogger().debug("Called routine RenameMethodForOperationSignatureEffect with input:");
    getLogger().debug("   OperationSignature: " + this.operationSignature);
    
    InterfaceMethod interfaceMethod = getCorrespondingElement(
    	getCorrepondenceSourceInterfaceMethod(operationSignature), // correspondence source supplier
    	InterfaceMethod.class,
    	(InterfaceMethod _element) -> true, // correspondence precondition checker
    	null);
    if (interfaceMethod == null) {
    	return;
    }
    initializeRetrieveElementState(interfaceMethod);
    
    preprocessElementStates();
    new mir.routines.pcm2java.RenameMethodForOperationSignatureEffect.EffectUserExecution(getExecutionState(), this).executeUserOperations(
    	operationSignature, interfaceMethod);
    new mir.routines.pcm2java.RenameMethodForOperationSignatureEffect.CallRoutinesUserExecution(getExecutionState(), this).executeUserOperations(
    	operationSignature, interfaceMethod);
    postprocessElementStates();
  }
}
