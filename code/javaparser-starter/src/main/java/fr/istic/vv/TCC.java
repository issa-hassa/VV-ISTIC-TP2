package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TCC extends VoidVisitorWithDefaults<Void> {

 StringBuilder htmlContent;
 int ccGlobale = 0;
 int nbMethod = 0;
    public TCC(StringBuilder htmlContent) {
        this.htmlContent = htmlContent;

        htmlContent.append("<tr>"+

                                "<th>Package name</th>\n" +
                                "<th>Class name</th>\n" +
                                "<th>TCC</th>\n"+
                            "</tr>"

        );
        htmlContent.append("<canvas id=\"ccChart\"></canvas>\n" +
                "<script>\n" +
                "var ctx = document.getElementById('ccChart').getContext('2d');\n" +
                "var myChart = new Chart(ctx, {\n" +
                "    type: 'bar',\n" +
                "    data: {\n" +
                "        labels: [],\n" +
                "        datasets: [{\n" +
                "            label: 'CC',\n" +
                "            data: [],\n" +
                "            backgroundColor: 'rgba(75, 192, 192, 0.2)',\n" +
                "            borderColor: 'rgba(75, 192, 192, 1)',\n" +
                "            borderWidth: 1\n" +
                "        }]\n" +
                "    },\n" +
                "    options: {\n" +
                "        scales: {\n" +
                "            y: {\n" +
                "                beginAtZero: true\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "});\n" +
                "</script>\n");

    }
/*
*  <tr>
    <th>Company</th>
    <th>Contact</th>
    <th>Country</th>
  </tr>*/
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for (TypeDeclaration<?> type : unit.getTypes()) {
            if (type instanceof ClassOrInterfaceDeclaration) {
                type.accept(this, null);
            }
        }
//        System.out.println("un truc : " + unit.toString());



    }

    public void visitTypeDeclaration(TypeDeclaration<?> declaration, Void arg) {
        // ...

        String path = declaration.getFullyQualifiedName().orElse("[Anonymous]");

        //     System.out.println(path);
        String names[] =  path.split("\\.");
        // Arrays.stream(names).forEach(e-> System.out.print(e));
        String className =names[names.length -1];
        String pathName =path.substring(0,path.lastIndexOf('.'));
        List<MethodDeclaration> methods = new ArrayList<>(declaration.getMethods());

        for (int i = 0; i < methods.size(); i++) {
            for (int j = i + 1; j < methods.size(); j++) {
                MethodDeclaration method1 = methods.get(i);
                MethodDeclaration method2 = methods.get(j);

                int commonVariables = countCommonVariables(method1, method2);

                // Increment M_total for each pair
                nbMethod++;

                // Increment M_same if the methods access the same instance variables
                if (commonVariables > 0) {
                    ccGlobale++;
                }
            }

        }

        htmlContent.append("<tr>");
        htmlContent.append(
                "<td>" + escapeHtml(pathName)+"</td>"+
                "<td>" + escapeHtml(className)+"</td>"+
                "<td>" +  ((double)ccGlobale)/nbMethod+"</td>"


        );
        htmlContent.append("</tr>\n");
        htmlContent.append("<script>");
        htmlContent.append("myChart.data.labels.push('" + className + "');\n");
        htmlContent.append("myChart.data.datasets[0].data.push(" + ((double)ccGlobale)/nbMethod + ");\n");
        htmlContent.append("</script>");

        // ...
    }

    private int countCommonVariables(MethodDeclaration method1, MethodDeclaration method2) {
        Set<String> variables1 = getUsedVariables(method1);
        Set<String> variables2 = getUsedVariables(method2);

        // Intersect the sets to find common variables
        variables1.retainAll(variables2);

        return variables1.size();
    }

    private Set<String> getUsedVariables(MethodDeclaration method) {
        Set<String> variables = new HashSet<>();

        if (method.getBody().isPresent()) {
            NodeList<Statement> statements = method.getBody().get().getStatements();

            for (Statement statement : statements) {
                // Assuming variables are simple identifiers (may need to refine based on your specific code)
                statement.findAll(NameExpr.class)
                        .forEach(nameExpr -> variables.add(nameExpr.getNameAsString()));
            }
        }

        return variables;
    }


    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        visitTypeDeclaration(declaration, arg);
    }

    public int getCcGlobale() {
        return ccGlobale;
    }

    public int getNbMethod() {
        return nbMethod;
    }

    public static String escapeHtml(String input) {
        if (input == null) {
            return null;
        }

        return input
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}

