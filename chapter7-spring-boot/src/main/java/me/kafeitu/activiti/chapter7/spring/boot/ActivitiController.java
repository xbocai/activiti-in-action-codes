package me.kafeitu.activiti.chapter7.spring.boot;

import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.catalina.manager.JspHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Henry Yan
 */
@Controller
@RequestMapping("/activiti")
public class ActivitiController {

    @Autowired
    ManagementService managementService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @ResponseBody
    @RequestMapping("/engine/info")
    public Map<String, String> engineProperties() {
        return managementService.getProperties();
    }

    @RequestMapping("/processes")
    public ModelAndView processes() {
        ModelAndView mav = new ModelAndView("processes");
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        mav.addObject("processes", list);
        return mav;
    }

    @RequestMapping("/process/start/{processDefinitionId}")
    public String startProcess(@PathVariable("processDefinitionId") String processDefinitionId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("create", "123");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, paramMap);
        System.out.println("成功启动了流程：" + processInstance.getId());
        return "redirect:/activiti/tasks";
    }

    @RequestMapping("/tasks")
    public ModelAndView tasks() {
        ModelAndView mav = new ModelAndView("tasks");
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list) {
            System.out.println("TaskVaris:" + task.getProcessVariables());
            System.out.println("LocalTaskVaris:" + task.getTaskLocalVariables());
        }
        mav.addObject("tasks", list);
        return mav;
    }

    @RequestMapping("/task/complete/{taskId}")
    public String completeTask(@PathVariable("taskId") String taskId) {
        Map<String, Object> map = new HashMap<>();
        map.put("comilete", taskId);
        taskService.complete(taskId, map);
        return "redirect:/activiti/tasks";
    }

}
