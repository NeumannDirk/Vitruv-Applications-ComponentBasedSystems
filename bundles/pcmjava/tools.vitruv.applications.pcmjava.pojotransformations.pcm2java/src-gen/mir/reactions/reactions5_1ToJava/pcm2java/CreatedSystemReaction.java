package mir.reactions.reactions5_1ToJava.pcm2java;

import mir.routines.pcm2java.RoutinesFacade;
import org.eclipse.xtext.xbase.lib.Extension;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractReactionRealization;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;
import tools.vitruv.framework.change.echange.EChange;
import tools.vitruv.framework.change.echange.compound.CreateAndInsertRoot;
import tools.vitruv.framework.change.echange.root.InsertRootEObject;
import tools.vitruv.framework.userinteraction.UserInteracting;

@SuppressWarnings("all")
class CreatedSystemReaction extends AbstractReactionRealization {
  public CreatedSystemReaction(final UserInteracting userInteracting) {
    super(userInteracting);
  }
  
  public void executeReaction(final EChange change) {
    CreateAndInsertRoot<org.palladiosimulator.pcm.system.System> typedChange = (CreateAndInsertRoot<org.palladiosimulator.pcm.system.System>)change;
    mir.routines.pcm2java.RoutinesFacade routinesFacade = new mir.routines.pcm2java.RoutinesFacade(this.executionState, this);
    mir.reactions.reactions5_1ToJava.pcm2java.CreatedSystemReaction.ActionUserExecution userExecution = new mir.reactions.reactions5_1ToJava.pcm2java.CreatedSystemReaction.ActionUserExecution(this.executionState, this);
    userExecution.callRoutine1(typedChange, routinesFacade);
  }
  
  public static Class<? extends EChange> getExpectedChangeType() {
    return CreateAndInsertRoot.class;
  }
  
  private boolean checkChangeProperties(final CreateAndInsertRoot<org.palladiosimulator.pcm.system.System> change) {
    if (!(change.getCreateChange().getAffectedEObject() instanceof org.palladiosimulator.pcm.system.System)) {
    	return false;
    }
    if (!(change.getInsertChange().getNewValue() instanceof org.palladiosimulator.pcm.system.System)) {
    	return false;
    }
    
    return true;
  }
  
  public boolean checkPrecondition(final EChange change) {
    if (!(change instanceof CreateAndInsertRoot)) {
    	return false;
    }
    CreateAndInsertRoot<org.palladiosimulator.pcm.system.System> typedChange = (CreateAndInsertRoot<org.palladiosimulator.pcm.system.System>)change;
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
    
    public void callRoutine1(final CreateAndInsertRoot<org.palladiosimulator.pcm.system.System> change, @Extension final RoutinesFacade _routinesFacade) {
      InsertRootEObject<org.palladiosimulator.pcm.system.System> _insertChange = change.getInsertChange();
      final org.palladiosimulator.pcm.system.System system = _insertChange.getNewValue();
      String _entityName = system.getEntityName();
      _routinesFacade.createJavaPackage(system, null, _entityName, "root_system");
      _routinesFacade.createImplementationForSystem(system);
    }
  }
}
