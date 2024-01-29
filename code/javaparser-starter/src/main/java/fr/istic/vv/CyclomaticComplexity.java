/*
package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CyclomaticComplexity extends VoidVisitorWithDefaults<Void> {

 StringBuilder htmlContent;
 int ccGlobale = 0;
 int nbMethod = 0;
    public CyclomaticComplexity(StringBuilder htmlContent) {
        this.htmlContent = htmlContent;

        htmlContent.append("<tr>"+
                                "<th> Method name    </th>\n"+
                                "<th>Package name</th>\n" +
                                "<th>Class name</th>\n" +
                                " <th>Parameters type</th>\n"+
                                "<th>CC</th>\n"+
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
  </tr>
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
        //if (!declaration.isPublic()) return;

        String path = declaration.getFullyQualifiedName().orElse("[Anonymous]");

   //     System.out.println(path);
        String names[] =  path.split("\\.");
       // Arrays.stream(names).forEach(e-> System.out.print(e));
        String className =names[names.length -1];
        String pathName =path.substring(0,path.lastIndexOf('.'));



        for (MethodDeclaration method : declaration.getMethods()) {
            nbMethod++;
            String methodName = method.getName().toString();
            String types ="";

            System.out.println(method.getParameters().size());
            for(Parameter tp : method.getParameters()){
                types+=tp.getType().toString()+",";
             //   System.out.println(tp.getType().toString());

            }
            if(!types.isEmpty()) {
                types = types.substring(0,types.length()-1);
            }
            int nbIf = 0;
            if(method.getBody().isPresent()){
                NodeList<Statement> statements = method.getBody().get().getStatements();
                for(Statement statement : statements){
                    if((statement instanceof IfStmt)
                            || (statement instanceof ForStmt)
                            ||(statement instanceof WhileStmt)
                            ||(statement) instanceof DoStmt)
                     nbIf++;
                }
            }

            int cc = nbIf + 1;
            ccGlobale+=cc;

          //  System.out.println("Method: " + methodName + ", Types: " + types);
            htmlContent.append("<tr>");
            htmlContent.append("<td>" + escapeHtml(methodName)+"</td>\n"+
                            "<td>" + escapeHtml(pathName)+"</td>\n"+
                            "<td>" + escapeHtml(className) +"</td>\n"+
                            "<td>" + escapeHtml(types)+"</td>\n"+
                            "<td>" + cc+"</td>\n"

                    );
            htmlContent.append("</tr>\n");
            htmlContent.append("<script>\n");
            htmlContent.append("myChart.data.labels.push('" + methodName + "');\n");
            htmlContent.append("myChart.data.datasets[0].data.push(" + cc + ");\n");
            htmlContent.append("</script>");

        }
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
*/

package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CyclomaticComplexity extends VoidVisitorWithDefaults<Void> {

    StringBuilder htmlContent;
    int ccGlobale = 0;
    int nbMethod = 0;
    List<String[]> dataRawList = new ArrayList<>();  // Add a List to store dataRaw entries

    public CyclomaticComplexity(StringBuilder htmlContent) {
        this.htmlContent = htmlContent;

        htmlContent.append("<tr>"+
                "<th> Method name    </th>\n"+
                "<th>Package name</th>\n" +
                "<th>Class name</th>\n" +
                " <th>Parameters type</th>\n"+
                "<th>CC</th>\n"+
                "</tr>"

        );



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
        //if (!declaration.isPublic()) return;

        String path = declaration.getFullyQualifiedName().orElse("[Anonymous]");

        //     System.out.println(path);
        String names[] =  path.split("\\.");
        // Arrays.stream(names).forEach(e-> System.out.print(e));
        String className =names[names.length -1];
        String pathName =path.substring(0,path.lastIndexOf('.'));



        for (MethodDeclaration method : declaration.getMethods()) {
            nbMethod++;
            String methodName = method.getName().toString();
            String types ="";

            //System.out.println(method.getParameters().size());
            for(Parameter tp : method.getParameters()){
                types+=tp.getType().toString()+",";
                //   System.out.println(tp.getType().toString());

            }
            if(!types.isEmpty()) {
                types = types.substring(0,types.length()-1);
            }
            int nbIf = 0;
            if(method.getBody().isPresent()){
                NodeList<Statement> statements = method.getBody().get().getStatements();
                for(Statement statement : statements){
                    if((statement instanceof IfStmt)
                            || (statement instanceof ForStmt)
                            ||(statement instanceof WhileStmt)
                            ||(statement) instanceof DoStmt)
                        nbIf++;
                }
            }

            int cc = nbIf + 1;
            ccGlobale+=cc;

            //  System.out.println("Method: " + methodName + ", Types: " + types);
            htmlContent.append("<tr>");
            htmlContent.append("<td>" + escapeHtml(methodName)+"</td>\n"+
                    "<td>" + escapeHtml(pathName)+"</td>\n"+
                    "<td>" + escapeHtml(className) +"</td>\n"+
                    "<td>" + escapeHtml(types)+"</td>\n"+
                    "<td>" + cc+"</td>\n"

            );
            htmlContent.append("</tr>\n");
            dataRawList.add(new String[]{methodName, String.valueOf(cc)});


        }
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

    public List<String[]> getDataRawList() {
        return dataRawList;
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


