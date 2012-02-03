package com.github.avereshchagin.emblang.actions;

import com.github.avereshchagin.emblang.controlflow.AssignmentInstruction;
import com.github.avereshchagin.emblang.controlflow.ControlFlowBuilder;
import com.github.avereshchagin.emblang.controlflow.Instruction;
import com.github.avereshchagin.emblang.controlflow.InstructionVisitor;
import com.github.avereshchagin.emblang.graph.Graph;
import com.github.avereshchagin.emblang.graph.GraphNode;
import com.github.avereshchagin.emblang.graph.GraphUtils;
import com.github.avereshchagin.emblang.graph.ReversedGraph;
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

        final Graph<Instruction> graph = com.github.avereshchagin.emblang.graph.GraphBuilder.buildFromControlFlow(controlFlowBuilder.getControlFlow());
        final Graph<Instruction> reversedGraph = new ReversedGraph<Instruction>(graph);
        GraphUtils.showGraph(reversedGraph);

        final RegularExpressionBuilder regularExpressionBuilder = new RegularExpressionBuilder(reversedGraph);
        for (final GraphNode node : graph.getNodes()) {
            graph.getValue(node).accept(new InstructionVisitor<Void>() {
                @Override
                public Void visitAssignmentInstruction(AssignmentInstruction instruction) {
                    if (instruction.isVerificationRequired()) {
                        RegexNode expression = regularExpressionBuilder.processGraphNode(node);
                        System.out.println(RegularExpressionBuilder.simplifyRegularExpression(expression));
                    }
                    return null;
                }
            });
        }
    }
}
