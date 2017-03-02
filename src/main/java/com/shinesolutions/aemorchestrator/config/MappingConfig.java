package com.shinesolutions.aemorchestrator.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shinesolutions.aemorchestrator.actions.ScaleAction;
import com.shinesolutions.aemorchestrator.actions.ScaleDownAuthorDispatcherAction;
import com.shinesolutions.aemorchestrator.actions.ScaleDownPublishAction;
import com.shinesolutions.aemorchestrator.actions.ScaleDownPublishDispatcherAction;
import com.shinesolutions.aemorchestrator.actions.ScaleUpAuthorDispatcherAction;
import com.shinesolutions.aemorchestrator.actions.ScaleUpPublishAction;
import com.shinesolutions.aemorchestrator.actions.ScaleUpPublishDispatcherAction;
import com.shinesolutions.aemorchestrator.handler.AutoScalingLaunchEventHandler;
import com.shinesolutions.aemorchestrator.handler.AutoScalingTerminateEventHandler;
import com.shinesolutions.aemorchestrator.handler.EventHandler;
import com.shinesolutions.aemorchestrator.model.EnvironmentValues;
import com.shinesolutions.aemorchestrator.model.EventType;

@Configuration
public class MappingConfig {

    @Bean
    public Map<String, EventHandler> eventTypeHandlerMappings(
        final AutoScalingTerminateEventHandler scaleDownEventHandler,
        final AutoScalingLaunchEventHandler scaleUpEventHandler) {

        Map<String, EventHandler> mappings = new HashMap<String, EventHandler>();
        mappings.put(EventType.AUTOSCALING_EC2_INSTANCE_TERMINATE.getValue(), scaleDownEventHandler);
        mappings.put(EventType.AUTOSCALING_EC2_INSTANCE_LAUNCH.getValue(), scaleUpEventHandler);

        return mappings;
    }

    @Bean(name="scaleDownAutoScaleGroupMappings")
    public Map<String, ScaleAction> scaleDownAutoScaleGroupMappings(
        final ScaleDownPublishDispatcherAction scaleDownPublishDispatcherAction,
        final ScaleDownPublishAction scaleDownPublishAction,
        final ScaleDownAuthorDispatcherAction scaleDownAuthorDispatcherAction,
        final EnvironmentValues envValues) {

        Map<String, ScaleAction> mappings = new HashMap<String, ScaleAction>();
        
        mappings.put(envValues.getAutoScaleGroupNameForPublishDispatcher(), scaleDownPublishDispatcherAction);
        mappings.put(envValues.getAutoScaleGroupNameForPublish(), scaleDownPublishAction);
        mappings.put(envValues.getAutoScaleGroupNameForAuthorDispatcher(), scaleDownAuthorDispatcherAction);

        return mappings;
    }
    
    @Bean(name="scaleUpAutoScaleGroupMappings")
    public Map<String, ScaleAction> scaleUpAutoScaleGroupMappings(
        final ScaleUpPublishDispatcherAction scaleUpPublishDispatcherAction,
        final ScaleUpPublishAction scaleUpPublishAction,
        final ScaleUpAuthorDispatcherAction scaleUpAuthorDispatcherAction,
        final EnvironmentValues envValues) {

        Map<String, ScaleAction> mappings = new HashMap<String, ScaleAction>();
   
        mappings.put(envValues.getAutoScaleGroupNameForPublishDispatcher(), scaleUpPublishDispatcherAction);
        mappings.put(envValues.getAutoScaleGroupNameForPublish(), scaleUpPublishAction);
        mappings.put(envValues.getAutoScaleGroupNameForAuthorDispatcher(), scaleUpAuthorDispatcherAction);

        return mappings;
    }
}
