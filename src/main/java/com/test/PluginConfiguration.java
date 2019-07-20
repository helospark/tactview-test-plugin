package com.test;

import java.util.List;

import com.helospark.lightdi.annotation.Bean;
import com.helospark.lightdi.annotation.Configuration;
import com.helospark.tactview.core.timeline.TimelineClipType;
import com.helospark.tactview.core.timeline.TimelineInterval;
import com.helospark.tactview.core.timeline.TimelineLength;
import com.helospark.tactview.core.timeline.effect.StandardEffectFactory;
import com.helospark.tactview.core.timeline.effect.TimelineEffectType;
import com.helospark.tactview.core.util.IndependentPixelOperation;

@Configuration
public class PluginConfiguration {

    @Bean
    public StandardEffectFactory blurEffect(IndependentPixelOperation independentPixelOperation) {
        System.out.println("Created");
        return StandardEffectFactory.builder()
                .withFactory(request -> new RedStatelessVideoEffect(new TimelineInterval(request.getPosition(), TimelineLength.ofMillis(10000)), independentPixelOperation))
                .withRestoreFactory((node, loadMetadata) -> new RedStatelessVideoEffect(node, loadMetadata, independentPixelOperation))
                .withName("Red effect")
                .withSupportedEffectId("redeffect")
                .withSupportedClipTypes(List.of(TimelineClipType.VIDEO, TimelineClipType.IMAGE))
                .withEffectType(TimelineEffectType.VIDEO_EFFECT)
                .build();
    }

}
