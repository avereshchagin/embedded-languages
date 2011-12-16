package com.github.avereshchagin.emblang.actions;

import com.github.avereshchagin.emblang.controlflow.ControlFlowBuilder;
import com.github.avereshchagin.emblang.graph.*;
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

        GraphFromControlFlowBuilder builder = new GraphFromControlFlowBuilder(controlFlowBuilder.getControlFlow());
        Graph<NodeData> graph = builder.getGraph();
        GraphUtils.showGraph(graph);

        RegularExpressionBuilder regularExpressionBuilder = new RegularExpressionBuilder();

        for (Node<NodeData> node : graph.getNodes()) {
            if (node.getData().isVerificationRequired()) {
                RegexNode expression = regularExpressionBuilder.buildRegularExpression(graph, node);
                System.out.println(expression);
            }
        }
    }
}
