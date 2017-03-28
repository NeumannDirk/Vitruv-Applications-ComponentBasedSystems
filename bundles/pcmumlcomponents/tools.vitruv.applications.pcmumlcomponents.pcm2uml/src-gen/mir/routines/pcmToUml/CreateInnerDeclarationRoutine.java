package mir.routines.pcmToUml;

import java.io.IOException;
import mir.routines.pcmToUml.RoutinesFacade;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.xtext.xbase.lib.Extension;
import org.palladiosimulator.pcm.repository.CompositeDataType;
import org.palladiosimulator.pcm.repository.InnerDeclaration;
import tools.vitruv.extensions.dslsruntime.reactions.AbstractRepairRoutineRealization;
import tools.vitruv.extensions.dslsruntime.reactions.ReactionExecutionState;
import tools.vitruv.extensions.dslsruntime.reactions.structure.CallHierarchyHaving;

@SuppressWarnings("all")
public class CreateInnerDeclarationRoutine extends AbstractRepairRoutineRealization {
  private RoutinesFacade actionsFacade;
  
  private CreateInnerDeclarationRoutine.ActionUserExecution userExecution;
  
  private static class ActionUserExecution extends AbstractRepairRoutineRealization.UserExecution {
    public ActionUserExecution(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy) {
      super(reactionExecutionState);
    }
    
    public EObject getCorrepondenceSourceUmlType(final InnerDeclaration innerDeclaration, final DataType compositeType) {
      org.palladiosimulator.pcm.repository.DataType _datatype_InnerDeclaration = innerDeclaration.getDatatype_InnerDeclaration();
      return _datatype_InnerDeclaration;
    }
    
    public EObject getCorrepondenceSourceCompositeType(final InnerDeclaration innerDeclaration) {
      CompositeDataType _compositeDataType_InnerDeclaration = innerDeclaration.getCompositeDataType_InnerDeclaration();
      return _compositeDataType_InnerDeclaration;
    }
    
    public void callRoutine1(final InnerDeclaration innerDeclaration, final DataType compositeType, final DataType umlType, @Extension final RoutinesFacade _routinesFacade) {
      _routinesFacade.createUmlPropertyForDatatype(umlType, innerDeclaration, compositeType);
    }
  }
  
  public CreateInnerDeclarationRoutine(final ReactionExecutionState reactionExecutionState, final CallHierarchyHaving calledBy, final InnerDeclaration innerDeclaration) {
    super(reactionExecutionState, calledBy);
    this.userExecution = new mir.routines.pcmToUml.CreateInnerDeclarationRoutine.ActionUserExecution(getExecutionState(), this);
    this.actionsFacade = new mir.routines.pcmToUml.RoutinesFacade(getExecutionState(), this);
    this.innerDeclaration = innerDeclaration;
  }
  
  private InnerDeclaration innerDeclaration;
  
  protected void executeRoutine() throws IOException {
    getLogger().debug("Called routine CreateInnerDeclarationRoutine with input:");
    getLogger().debug("   InnerDeclaration: " + this.innerDeclaration);
    
    DataType compositeType = getCorrespondingElement(
    	userExecution.getCorrepondenceSourceCompositeType(innerDeclaration), // correspondence source supplier
    	DataType.class,
    	(DataType _element) -> true, // correspondence precondition checker
    	null);
    if (compositeType == null) {
    	return;
    }
    registerObjectUnderModification(compositeType);
    DataType umlType = getCorrespondingElement(
    	userExecution.getCorrepondenceSourceUmlType(innerDeclaration, compositeType), // correspondence source supplier
    	DataType.class,
    	(DataType _element) -> true, // correspondence precondition checker
    	null);
    if (umlType == null) {
    	return;
    }
    registerObjectUnderModification(umlType);
    userExecution.callRoutine1(innerDeclaration, compositeType, umlType, actionsFacade);
    
    postprocessElements();
  }
}
