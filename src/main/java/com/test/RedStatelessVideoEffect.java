package com.test;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.helospark.tactview.core.clone.CloneRequestMetadata;
import com.helospark.tactview.core.save.LoadMetadata;
import com.helospark.tactview.core.timeline.StatelessEffect;
import com.helospark.tactview.core.timeline.StatelessVideoEffect;
import com.helospark.tactview.core.timeline.TimelineInterval;
import com.helospark.tactview.core.timeline.effect.StatelessEffectRequest;
import com.helospark.tactview.core.timeline.effect.interpolation.ValueProviderDescriptor;
import com.helospark.tactview.core.timeline.effect.interpolation.interpolator.MultiKeyframeBasedDoubleInterpolator;
import com.helospark.tactview.core.timeline.effect.interpolation.provider.BooleanProvider;
import com.helospark.tactview.core.timeline.image.ClipImage;
import com.helospark.tactview.core.timeline.image.ReadOnlyClipImage;
import com.helospark.tactview.core.util.IndependentPixelOperation;
import com.helospark.tactview.core.util.ReflectionUtil;

public class RedStatelessVideoEffect extends StatelessVideoEffect {
    private BooleanProvider shouldDoIt;
    private IndependentPixelOperation independentPixelOperation;

    public RedStatelessVideoEffect(TimelineInterval interval, IndependentPixelOperation independentPixelOperation) {
        super(interval);
        this.independentPixelOperation = independentPixelOperation;

    }

    public RedStatelessVideoEffect(RedStatelessVideoEffect redStatelessVideoEffect, CloneRequestMetadata cloneRequestMetadata) {
        super(redStatelessVideoEffect, cloneRequestMetadata);
        ReflectionUtil.copyOrCloneFieldFromTo(redStatelessVideoEffect, this, cloneRequestMetadata);
    }

    public RedStatelessVideoEffect(JsonNode node, LoadMetadata loadMetadata, IndependentPixelOperation independentPixelOperation) {
        super(node, loadMetadata);
        this.independentPixelOperation = independentPixelOperation;
    }

    @Override
    public ReadOnlyClipImage createFrame(StatelessEffectRequest request) {
        ClipImage result = ClipImage.sameSizeAs(request.getCurrentFrame());

        if (shouldDoIt.getValueAt(request.getEffectPosition())) {
            independentPixelOperation.executePixelTransformation(result.getWidth(), result.getHeight(), (x, y) -> {
                result.setRed(255, x, y);
                result.setAlpha(255, x, y);
            });
        } else {
            independentPixelOperation.executePixelTransformation(result.getWidth(), result.getHeight(), (x, y) -> {
                result.copyColorFrom(request.getCurrentFrame(), x, y, x, y);
            });
        }

        return result;
    }

    @Override
    public StatelessEffect cloneEffect(CloneRequestMetadata cloneRequestMetadata) {
        return new RedStatelessVideoEffect(this, cloneRequestMetadata);
    }

    @Override
    protected void initializeValueProviderInternal() {
        shouldDoIt = new BooleanProvider(new MultiKeyframeBasedDoubleInterpolator(1.0));
    }

    @Override
    protected List<ValueProviderDescriptor> getValueProvidersInternal() {
        ValueProviderDescriptor shouldDoDescriptor = ValueProviderDescriptor.builder()
                .withKeyframeableEffect(shouldDoIt)
                .withName("Should do")
                .build();
        return List.of(shouldDoDescriptor);
    }

}
