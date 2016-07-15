package mir.routines.pcm2java;

import edu.kit.ipd.sdq.vitruvius.dsls.response.runtime.AbstractEffectRealization;
import edu.kit.ipd.sdq.vitruvius.dsls.response.runtime.ResponseExecutionState;
import edu.kit.ipd.sdq.vitruvius.dsls.response.runtime.structure.CallHierarchyHaving;
import edu.kit.ipd.sdq.vitruvius.framework.meta.change.feature.attribute.UpdateSingleValuedEAttribute;
import java.io.IOException;
import mir.routines.pcm2java.RoutinesFacade;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class ChangedSystemNameEffect extends AbstractEffectRealization {
  public ChangedSystemNameEffect(final ResponseExecutionState responseExecutionState, final CallHierarchyHaving calledBy, final UpdateSingleValuedEAttribute<String> change) {
    super(responseExecutionState, calledBy);
    				this.change = change;
  }
  
  private UpdateSingleValuedEAttribute<String> change;
  
  private EObject getCorrepondenceSourceSystemPackage(final UpdateSingleValuedEAttribute<String> change) {
    EObject _newAffectedEObject = change.getNewAffectedEObject();
    return _newAffectedEObject;
  }
  
  protected void executeRoutine() throws IOException {
    getLogger().debug("Called routine ChangedSystemNameEffect with input:");
    getLogger().debug("   UpdateSingleValuedEAttribute: " + this.change);
    
    org.emftext.language.java.containers.Package systemPackage = getCorrespondingElement(
    	getCorrepondenceSourceSystemPackage(change), // correspondence source supplier
    	org.emftext.language.java.containers.Package.class,
    	(org.emftext.language.java.containers.Package _element) -> true, // correspondence precondition checker
    	null);
    if (systemPackage == null) {
    	return;
    }
    initializeRetrieveElementState(systemPackage);
    
    preprocessElementStates();
    new mir.routines.pcm2java.ChangedSystemNameEffect.EffectUserExecution(getExecutionState(), this).executeUserOperations(
    	change, systemPackage);
    postprocessElementStates();
  }
  
  private static class EffectUserExecution extends AbstractEffectRealization.UserExecution {
    @Extension
    private RoutinesFacade effectFacade;
    
    public EffectUserExecution(final ResponseExecutionState responseExecutionState, final CallHierarchyHaving calledBy) {
      super(responseExecutionState);
      this.effectFacade = new mir.routines.pcm2java.RoutinesFacade(responseExecutionState, calledBy);
    }
    
    private void executeUserOperations(final UpdateSingleValuedEAttribute<String> change, final org.emftext.language.java.containers.Package systemPackage) {
      EObject _newAffectedEObject = change.getNewAffectedEObject();
      final org.palladiosimulator.pcm.system.System system = ((org.palladiosimulator.pcm.system.System) _newAffectedEObject);
      String _entityName = system.getEntityName();
      this.effectFacade.callRenameJavaPackage(system, null, _entityName, null);
      String _entityName_1 = system.getEntityName();
      String _plus = (_entityName_1 + "Impl");
      this.effectFacade.callRenameJavaClassifier(system, systemPackage, _plus);
    }
  }
}
