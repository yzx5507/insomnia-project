package com.insomnia;

import com.insomnia.enums.SpringMappingEnum;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Arrays;

/**
 * @author Jay  zhixin.yuan@transsnet.com
 * @project: insomnia-project
 * @description:
 * @date 2023/11/22
 */
public class GenerateAction extends AnAction {

    private static final Logger log = Logger.getInstance(GenerateAction.class);

    Project project;
    PsiFile psiFile;
    PsiElement psiElement;

    @Override
    public void actionPerformed(AnActionEvent e) {
        String result = "Not support yet", title = "Generate";

        project = e.getData(PlatformDataKeys.PROJECT);
        psiFile = e.getData(CommonDataKeys.PSI_FILE);
        psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        PsiClass psiClass = PsiTreeUtil.findChildOfAnyType(psiFile, PsiClass.class);

        if (project == null || psiFile == null || psiClass == null) {
            log.warn("no available project or file or class");
            return;
        }

        PsiModifierList modifierList = psiClass.getModifierList();
        if (modifierList == null) {
            log.warn("no method in class, pls check");
            return;
        }

        if (!(psiElement instanceof PsiMethodImpl)) {
            Messages.showMessageDialog(project, result, title, Messages.getInformationIcon());
            return;
        }

        PsiAnnotation requestMappingAnno = modifierList.findAnnotation(SpringMappingEnum.MAPPING.getCode());
        String requestUrl = getAttribute(requestMappingAnno, "value", "");
        PsiModifierList methodList = ((PsiMethodImpl) psiElement).getModifierList();

        PsiAnnotation currentAnno = Arrays.stream(methodList.getAnnotations())
                .filter(m -> StringUtils.isNotBlank(m.getQualifiedName())
                        && m.getQualifiedName().startsWith(SpringMappingEnum.MAPPING_PREFIX.getCode()))
                .findFirst().orElse(null);
        if (currentAnno == null) {
            log.error("occur some exception, methodList: {}", methodList.getText());
            Messages.showMessageDialog(project, "Unknown error, pls try again", title, Messages.getInformationIcon());
            return;
        }

        String currentMethodUrl = getAttribute(currentAnno, "value", "");
        result = (requestUrl + currentMethodUrl).replaceAll("//", "/");
        setSysClipboardText(result);
    }

    /**
     * 将字符串复制到剪切板
     */
    public static void setSysClipboardText(String text) {
        log.debug("write into system clipboard text" + text);
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(text);
        clip.setContents(tText, null);
    }


    /**
     * 获取注解属性
     *
     * @param psiAnnotation 注解全路径
     * @param attributeName 注解属性名
     * @return 属性值
     */
    private String getAttribute(PsiAnnotation psiAnnotation, String attributeName, String comment) {
        if (ObjectUtils.isEmpty(psiAnnotation)) {
            return comment;
        }
        PsiAnnotationMemberValue psiAnnotationMemberValue = psiAnnotation.findDeclaredAttributeValue(attributeName);
        if (ObjectUtils.isEmpty(psiAnnotationMemberValue)) {
            return comment;
        }
        return psiAnnotationMemberValue.getText().replaceAll("\"", "");
    }
}
