package com.github.avereshchagin.emblang.actions;

import com.github.avereshchagin.emblang.cfg.CfgFromInstructionsBuilder;
import com.github.avereshchagin.emblang.cfg.CfgRootStatement;
import com.github.avereshchagin.emblang.cfg.CfgStatement;
import com.github.avereshchagin.emblang.cfg.DepthFirstSearcher;
import com.github.avereshchagin.emblang.controlflow.ControlFlowBuilder;
import com.github.avereshchagin.emblang.regex.RegexNode;
import com.github.avereshchagin.emblang.regex.RegularExpressionBuilder;
import com.github.avereshchagin.emblang.verification.JDBCMethodsFinder;
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

        ControlFlowBuilder controlFlowBuilder = new ControlFlowBuilder(new JDBCMethodsFinder());
        JavaPsiFacade facade = JavaPsiFacadeEx.getInstance(project);
        PsiPackage defaultPackage = facade.findPackage("");
        if (defaultPackage != null) {
            for (PsiPackage psiPackage : defaultPackage.getSubPackages()) {
                for (PsiClass psiClass : psiPackage.getClasses()) {
                    for (PsiMethod psiMethod : psiClass.getMethods()) {
                        controlFlowBuilder.processMethod(psiMethod);

                    }
                }
            }
        }
        System.out.println(controlFlowBuilder.toString());
        CfgFromInstructionsBuilder builder = CfgFromInstructionsBuilder.fromControlFlow(controlFlowBuilder.getControlFlow());

        DepthFirstSearcher searcher = new DepthFirstSearcher();
        searcher.processGraph(builder.getControlFlowGraph());
        searcher.markLoops(builder.getControlFlowGraph());
        builder.showGraph();

        RegularExpressionBuilder regularExpressionBuilder = new RegularExpressionBuilder();

        for (CfgStatement statement : builder.getControlFlowGraph().getVerifiableMethodCallNodes()) {
            CfgRootStatement methodEntry = builder.getControlFlowGraph().getMethodEntries().get(0);
            RegexNode expression = regularExpressionBuilder.buildRegularExpression(methodEntry, statement, searcher.getTopOrdering());
            System.out.println(expression.toString());
//            System.out.println();
//            System.out.println("Regular expression:");
//            System.out.println(RegularExpressionBuilder.buildRegularExpression(node).toString());
        }
    }
}
