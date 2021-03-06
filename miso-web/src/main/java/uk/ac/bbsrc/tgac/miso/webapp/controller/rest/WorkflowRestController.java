package uk.ac.bbsrc.tgac.miso.webapp.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.eaglegenomics.simlims.core.User;
import com.eaglegenomics.simlims.core.manager.SecurityManager;

import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow;
import uk.ac.bbsrc.tgac.miso.core.data.workflow.Workflow.WorkflowName;
import uk.ac.bbsrc.tgac.miso.dto.Dtos;
import uk.ac.bbsrc.tgac.miso.dto.WorkflowStateDto;
import uk.ac.bbsrc.tgac.miso.service.security.AuthorizationManager;
import uk.ac.bbsrc.tgac.miso.service.workflow.WorkflowManager;

/**
 * Responsible for handling all workflow AJAX requests
 */
@Controller
@RequestMapping("/rest/workflow")
public class WorkflowRestController extends RestController {
  @Autowired
  WorkflowManager workflowManager;

  @Autowired
  private SecurityManager securityManager;

  @Autowired
  private AuthorizationManager authorizationManager;

  @RequestMapping(value = "/{workflowId}/step/{stepNumber}", method = RequestMethod.POST)
  public @ResponseBody WorkflowStateDto process(@PathVariable("workflowId") long workflowId, @PathVariable("stepNumber") int stepNumber,
      @RequestParam("input") String input) throws IOException {
    Workflow workflow = workflowManager.loadWorkflow(workflowId);
    workflowManager.processInput(workflow, stepNumber, input);

    return Dtos.asDto(workflow, stepNumber + 1);
  }

  @RequestMapping(value = "/{workflowId}/execute", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public @ResponseBody void execute(@PathVariable("workflowId") long workflowId) throws IOException {
    workflowManager.execute(workflowManager.loadWorkflow(workflowId));
  }

  @RequestMapping(value = "/{workflowId}/step/{stepNumber}", method = RequestMethod.GET)
  public @ResponseBody WorkflowStateDto getStep(@PathVariable("workflowId") long workflowId, @PathVariable("stepNumber") int stepNumber)
      throws IOException {
    Workflow workflow = workflowManager.loadWorkflow(workflowId);
    return Dtos.asDto(workflow, stepNumber);
  }

  @RequestMapping(value = "/{workflowId}/step/latest", method = RequestMethod.GET)
  public @ResponseBody WorkflowStateDto nextStep(@PathVariable("workflowId") long workflowId) throws IOException {
    return Dtos.asDto(workflowManager.loadWorkflow(workflowId));
  }

  @RequestMapping(value = "/{workflowId}/step/latest", method = RequestMethod.DELETE)
  public @ResponseBody WorkflowStateDto cancelInput(@PathVariable("workflowId") long workflowId) throws IOException {
    Workflow workflow = workflowManager.loadWorkflow(workflowId);
    workflowManager.cancelInput(workflow);
    return Dtos.asDto(workflow);
  }

  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public List<WorkflowStateDto> list() throws IOException {
    return workflowManager.listUserWorkflows().stream().map(Dtos::asDto).collect(Collectors.toList());
  }

  @RequestMapping(method = RequestMethod.POST, value = "/favourites/add/{workflowName}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void addFavourite(@PathVariable WorkflowName workflowName)
      throws IOException {
    User user = authorizationManager.getCurrentUser();
    Set<WorkflowName> favouriteWorkflows = user.getFavouriteWorkflows();
    favouriteWorkflows.add(workflowName);
    securityManager.saveUser(user);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/favourites/remove/{workflowName}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void removeFavourite(@PathVariable WorkflowName workflowName)
      throws IOException {
    User user = authorizationManager.getCurrentUser();
    Set<WorkflowName> favouriteWorkflows = user.getFavouriteWorkflows();
    favouriteWorkflows.remove(workflowName);
    securityManager.saveUser(user);
  }
}
