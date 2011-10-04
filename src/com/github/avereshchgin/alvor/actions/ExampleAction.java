package com.github.avereshchgin.alvor.actions;

import com.github.avereshchgin.alvor.cfg.ControlFlowGraph;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.JavaPsiFacadeEx;
import com.intellij.psi.search.GlobalSearchScope;

public class ExampleAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();
        if (project == null) {
            System.err.println("Cannot get project.");
            return;
        }

        JavaPsiFacade facade = JavaPsiFacadeEx.getInstance(project);
        PsiClass foo = facade.findClass("pac.Foo", GlobalSearchScope.allScope(project));
        if (foo == null) {
            System.err.println("Cannot find class pac.Foo in your project.");
            return;
        }

        ControlFlowGraph controlFlowGraph = new ControlFlowGraph();
        for (PsiMethod psiMethod : foo.getAllMethods()) {
            controlFlowGraph.addMethod(psiMethod);
        }
        controlFlowGraph.showGraph();
    }
}