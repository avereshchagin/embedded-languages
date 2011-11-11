package com.github.avereshchgin.alvor.actions;

import com.github.avereshchgin.alvor.cfg.CfgStatement;
import com.github.avereshchgin.alvor.cfg.ControlFlowGraphBuilder;
import com.github.avereshchgin.alvor.cfg.DepthFirstSearcher;
import com.github.avereshchgin.alvor.regex.RegularExpressionBuilder;
import com.github.avereshchgin.alvor.verification.JDBCMethodsFinder;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.impl.JavaPsiFacadeEx;

public class ExampleAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) {
            System.err.println("Cannot get project.");
            return;
        }

        ControlFlowGraphBuilder cfgBuilder = new ControlFlowGraphBuilder(new JDBCMethodsFinder());
        JavaPsiFacade facade = JavaPsiFacadeEx.getInstance(project);
        PsiPackage defaultPackage = facade.findPackage("");
        if (defaultPackage != null) {
            for (PsiPackage psiPackage : defaultPackage.getSubPackages()) {
                for (PsiClass psiClass : psiPackage.getClasses()) {
                    for (PsiMethod psiMethod : psiClass.getMethods()) {
                        cfgBuilder.addMethod(psiMethod);
                    }
                }
            }
        }
        cfgBuilder.showGraph();

        DepthFirstSearcher.processGraph(cfgBuilder.getControlFlowGraph());

        for (CfgStatement node : cfgBuilder.getControlFlowGraph().getVerifiableMethodCallNodes()) {
            System.out.println();
            System.out.println("Regular expression:");
            System.out.println(RegularExpressionBuilder.buildRegularExpression(node).toString());
        }
    }
}
