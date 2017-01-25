package mir.reactions.reactions5_1ToJava.pcm2java;

import mir.routines.pcm2java.RoutinesFacade;
import org.eclipse.xtext.xbase.lib.Extension;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.OperationSignature;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractReactionRealization;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;
import tools.vitruv.framework.change.echange.EChange;
import tools.vitruv.framework.change.echange.feature.reference.ReplaceSingleValuedEReference;
import tools.vitruv.framework.userinteraction.UserInteracting;

@SuppressWarnings("all")
class ChangeOperationSignatureReturnTypeReaction extends AbstractReactionRealization {
  public ChangeOperationSignatureReturnTypeReaction(final UserInteracting userInteracting) {
    super(userInteracting);
  }
  
  public void executeReaction(final EChange change) {
    ReplaceSingleValuedEReference<OperationSignature, DataType> typedChange = (ReplaceSingleValuedEReference<OperationSignature, DataType>)change;
    mir.routines.pcm2java.RoutinesFacade routinesFacade = new mir.routines.pcm2java.RoutinesFacade(this.executionState, this);
    mir.reactions.reactions5_1ToJava.pcm2java.ChangeOperationSignatureReturnTypeReaction.ActionUserExecution userExecution = new mir.reactions.reactions5_1ToJava.pcm2java.ChangeOperationSignatureReturnTypeReaction.ActionUserExecution(this.executionState, this);
    userExecution.callRoutine1(typedChange, routinesFacade);
  }
  
  public static Class<? extends EChange> getExpectedChangeType() {
    return ReplaceSingleValuedEReference.class;
  }
  
  private boolean checkChangeProperties(final ReplaceSingleValuedEReference<OperationSignature, DataType> change) {
    // Check affected object
    if (!(change.getAffectedEObject() instanceof OperationSignature)) {
    	return false;
    }
    // Check feature
    if (!change.getAffectedFeature().getName().equals("returnType__OperationSignature")) {
    	return false;
    }
    if (change.isFromNonDefaultValue() && !(change.getOldValue() instanceof DataType)
    ) {
    	return false;
    }
    if (change.isToNonDefaultValue() && !(change.getNewValue() instanceof DataType)) {
    	return false;
    }
    
    return true;
  }
  
  public boolean checkPrecondition(final EChange change) {
    if (!(change instanceof ReplaceSingleValuedEReference<?, ?>)) {
    	return false;
    }
    ReplaceSingleValuedEReference<OperationSignature, DataType> typedChange = (ReplaceSingleValuedEReference<OperationSignature, DataType>)change;
    if (!checkChangeProperties(typedChange)) {
    	return false;
    }
    getLogger().debug("Passed precondition check of reaction " + this.getClass().getName());
    return true;
  }
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public void callRoutine1(final ReplaceSingleValuedEReference<OperationSignature, DataType> change, @Extension final RoutinesFacade _routinesFacade) {
      OperationSignature _affectedEObject = change.getAffectedEObject();
      _routinesFacade.changeReturnTypeOfMethodForOperationSignature(_affectedEObject);
    }
  }
}
