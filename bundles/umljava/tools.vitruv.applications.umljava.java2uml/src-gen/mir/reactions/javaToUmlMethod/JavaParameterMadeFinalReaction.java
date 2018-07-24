package mir.reactions.javaToUmlMethod;

import mir.routines.javaToUmlMethod.RoutinesFacade;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.emftext.language.java.modifiers.Final;
import org.emftext.language.java.parameters.Parameter;
import tools.vitruv.applications.umljava.java2uml.JavaToUmlHelper;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractReactionRealization;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;
import tools.vitruv.framework.change.echange.EChange;
import tools.vitruv.framework.change.echange.eobject.CreateEObject;
import tools.vitruv.framework.change.echange.feature.reference.InsertEReference;

@SuppressWarnings("all")
public class JavaParameterMadeFinalReaction extends AbstractReactionRealization {
  private CreateEObject<Final> createChange;
  
  private InsertEReference<Parameter, Final> insertChange;
  
  private int currentlyMatchedChange;
  
  public JavaParameterMadeFinalReaction(final RoutinesFacade routinesFacade) {
    super(routinesFacade);
  }
  
  public void executeReaction(final EChange change) {
    if (!checkPrecondition(change)) {
    	return;
    }
    org.emftext.language.java.parameters.Parameter affectedEObject = insertChange.getAffectedEObject();
    EReference affectedFeature = insertChange.getAffectedFeature();
    org.emftext.language.java.modifiers.Final newValue = insertChange.getNewValue();
    int index = insertChange.getIndex();
    				
    getLogger().trace("Passed complete precondition check of Reaction " + this.getClass().getName());
    				
    mir.reactions.javaToUmlMethod.JavaParameterMadeFinalReaction.ActionUserExecution userExecution = new mir.reactions.javaToUmlMethod.JavaParameterMadeFinalReaction.ActionUserExecution(this.executionState, this);
    userExecution.callRoutine1(insertChange, affectedEObject, affectedFeature, newValue, index, this.getRoutinesFacade());
    
    resetChanges();
  }
  
  private void resetChanges() {
    createChange = null;
    insertChange = null;
    currentlyMatchedChange = 0;
  }
  
  private boolean matchCreateChange(final EChange change) {
    if (change instanceof CreateEObject<?>) {
    	CreateEObject<org.emftext.language.java.modifiers.Final> _localTypedChange = (CreateEObject<org.emftext.language.java.modifiers.Final>) change;
    	if (!(_localTypedChange.getAffectedEObject() instanceof org.emftext.language.java.modifiers.Final)) {
    		return false;
    	}
    	this.createChange = (CreateEObject<org.emftext.language.java.modifiers.Final>) change;
    	return true;
    }
    
    return false;
  }
  
  public boolean checkPrecondition(final EChange change) {
    if (currentlyMatchedChange == 0) {
    	if (!matchCreateChange(change)) {
    		resetChanges();
    		return false;
    	} else {
    		currentlyMatchedChange++;
    	}
    	return false; // Only proceed on the last of the expected changes
    }
    if (currentlyMatchedChange == 1) {
    	if (!matchInsertChange(change)) {
    		resetChanges();
    		checkPrecondition(change); // Reexecute to potentially register this as first change
    		return false;
    	} else {
    		currentlyMatchedChange++;
    	}
    }
    
    return true;
  }
  
  private boolean matchInsertChange(final EChange change) {
    if (change instanceof InsertEReference<?, ?>) {
    	InsertEReference<org.emftext.language.java.parameters.Parameter, org.emftext.language.java.modifiers.Final> _localTypedChange = (InsertEReference<org.emftext.language.java.parameters.Parameter, org.emftext.language.java.modifiers.Final>) change;
    	if (!(_localTypedChange.getAffectedEObject() instanceof org.emftext.language.java.parameters.Parameter)) {
    		return false;
    	}
    	if (!_localTypedChange.getAffectedFeature().getName().equals("annotationsAndModifiers")) {
    		return false;
    	}
    	if (!(_localTypedChange.getNewValue() instanceof org.emftext.language.java.modifiers.Final)) {
    		return false;
    	}
    	this.insertChange = (InsertEReference<org.emftext.language.java.parameters.Parameter, org.emftext.language.java.modifiers.Final>) change;
    	return true;
    }
    
    return false;
  }
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public void callRoutine1(final InsertEReference insertChange, final Parameter affectedEObject, final EReference affectedFeature, final Final newValue, final int index, @Extension final RoutinesFacade _routinesFacade) {
      JavaToUmlHelper.showMessage(this.userInteractor, ("Final parameters are not supported. Please remove the modifier from " + affectedEObject));
    }
  }
}
