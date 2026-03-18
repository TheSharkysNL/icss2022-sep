package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;



public class Checker {

    private final IHANLinkedList<HashMap<String, ExpressionType>> variableTypes = new HANLinkedList<>();


    public void check(AST ast) {


    }

    public static <T> void popVariablesOffStack(IHANLinkedList<HashMap<String, T>> variables, BodyStatement parent) {
        if (parent instanceof IfClause) { // only place variables in higher stacks if the variable was used within a if clause
            HashMap<String, T> lastOnStack = variables.getFirst();
            variables.removeFirst();
            HashMap<String, T> newLastOnStack = variables.getFirst();
            if (newLastOnStack != null) {
                for (Map.Entry<String, T> entry : lastOnStack.entrySet()) {
                    if (newLastOnStack.containsKey(entry.getKey())) { // if variable exists in a higher stack then replace its value
                        newLastOnStack.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        } else {
            variables.removeFirst();
        }
    }
}
