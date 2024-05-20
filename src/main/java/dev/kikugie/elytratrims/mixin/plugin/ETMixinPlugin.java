package dev.kikugie.elytratrims.mixin.plugin;

import dev.kikugie.elytratrims.common.ETReference;
import dev.kikugie.elytratrims.common.config.Tester;
import dev.kikugie.elytratrims.platform.ModStatus;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public class ETMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (shouldApply(mixinClassName)) return true;
        String shortName = StringUtils.substringAfter(mixinClassName, "mixin.");
        if (!shortName.startsWith("compat.")) // Reduce unneeded spam  
            ETReference.LOGGER.info("Disabled mixin %s".formatted(shortName));
        return false;
    }

    private boolean shouldApply(String mixin) {
//        AnnotationNode mixinConfigurable = getAnnotation(mixin, MixinConfigurable.class);  
//        boolean configResult = mixinConfigurable == null || !ServerConfigs.getMixinConfig().contains(mixin);  
//        if (!configResult) return false;  

        AnnotationNode modRequirement = getAnnotation(mixin, RequireMod.class);
        boolean modResult = modRequirement == null || ModStatus.INSTANCE.isLoaded(Annotations.getValue(modRequirement));
        if (!modResult) return false;

        AnnotationNode testerRequirement = getAnnotation(mixin, RequireTest.class);
        return testerRequirement == null || runTester(Annotations.getValue(testerRequirement));
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    private boolean runTester(Type type) {
        try {
            return Tester.runTest(Class.forName(type.getClassName()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private AnnotationNode getAnnotation(String className, Class<? extends Annotation> annotation) {
        try {
            ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(className);
            return Annotations.getVisible(classNode, annotation);
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }
}