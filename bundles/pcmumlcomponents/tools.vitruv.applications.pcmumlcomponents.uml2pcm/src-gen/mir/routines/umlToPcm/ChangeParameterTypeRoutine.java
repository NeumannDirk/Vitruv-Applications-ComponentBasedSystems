package mir.routines.umlToPcm;

import java.io.IOException;
import mir.routines.umlToPcm.RoutinesFacade;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;
import org.palladiosimulator.pcm.repository.DataType;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;

@SuppressWarnings("all")
public class ChangeParameterTypeRoutine extends AbstractRepairRoutineRealization {
  private RoutinesFacade actionsFacade;
  
  private ChangeParameterTypeRoutine.ActionUserExecution userExecution;
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public EObject getElement1(final Parameter umlParameter, final org.palladiosimulator.pcm.repository.Parameter pcmParameter, final DataType pcmType) {
      return pcmParameter;
    }
    
    public void update0Element(final Parameter umlParameter, final org.palladiosimulator.pcm.repository.Parameter pcmParameter, final DataType pcmType) {
      pcmParameter.setDataType__Parameter(pcmType);
    }
    
    public EObject getCorrepondenceSourcePcmType(final Parameter umlParameter, final org.palladiosimulator.pcm.repository.Parameter pcmParameter) {
      Type _type = umlParameter.getType();
      return _type;
    }
    
    public EObject getCorrepondenceSourcePcmParameter(final Parameter umlParameter) {
      return umlParameter;
    }
  }
  
  public ChangeParameterTypeRoutine(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy, final Parameter umlParameter) {
    super(reactionExecutionState, calledBy);
    this.userExecution = new mir.routines.umlToPcm.ChangeParameterTypeRoutine.ActionUserExecution(getExecutionState(), this);
    this.actionsFacade = new mir.routines.umlToPcm.RoutinesFacade(getExecutionState(), this);
    this.umlParameter = umlParameter;
  }
  
  private Parameter umlParameter;
  
  protected void executeRoutine() throws IOException {
    getLogger().debug("Called routine ChangeParameterTypeRoutine with input:");
    getLogger().debug("   Parameter: " + this.umlParameter);
    
    org.palladiosimulator.pcm.repository.Parameter pcmParameter = getCorrespondingElement(
    	userExecution.getCorrepondenceSourcePcmParameter(umlParameter), // correspondence source supplier
    	org.palladiosimulator.pcm.repository.Parameter.class,
    	(org.palladiosimulator.pcm.repository.Parameter _element) -> true, // correspondence precondition checker
    	null);
    if (pcmParameter == null) {
    	return;
    }
    registerObjectUnderModification(pcmParameter);
    DataType pcmType = getCorrespondingElement(
    	userExecution.getCorrepondenceSourcePcmType(umlParameter, pcmParameter), // correspondence source supplier
    	DataType.class,
    	(DataType _element) -> true, // correspondence precondition checker
    	null);
    if (pcmType == null) {
    	return;
    }
    registerObjectUnderModification(pcmType);
    // val updatedElement userExecution.getElement1(umlParameter, pcmParameter, pcmType);
    userExecution.update0Element(umlParameter, pcmParameter, pcmType);
    
    postprocessElements();
  }
}
