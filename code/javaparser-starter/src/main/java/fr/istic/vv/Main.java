package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static File resultfile;
    private static StringBuilder htmlContent;

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if (!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }
        //callTCC(file);
        callCC(file);
    }

    private static void writeHtmlToFile(String htmlContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultfile))) {
            writer.write(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void callCC(File file) throws IOException {
        resultfile = new File("resultCC.html");
        htmlContent = new StringBuilder("<html>" +
                "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">\n" +
                "</head>" +
                "<body><table>");

        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();
        CyclomaticComplexity membersWithoutGetter = new CyclomaticComplexity(htmlContent);

        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(membersWithoutGetter, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
        htmlContent.append("</table>");


        htmlContent.append("<div id=\"ccChart\"></div>\n");

        htmlContent.append(
                "<script  type=\"text/javascript\">"+
                "var dataRaw = [['Method name', 'CC']];\n"+
                "</script>"
        );
        htmlContent.append("<script  type=\"text/javascript\">\n");
        membersWithoutGetter.getDataRawList().forEach(entry -> {
            htmlContent.append("dataRaw.push(['" + entry[0] + "', " + entry[1] + "]);\n");
        });
        htmlContent.append("</script>");
        htmlContent.append("<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n");
        htmlContent.append("<script type=\"text/javascript\">\n");
        htmlContent.append("google.charts.load('current', {'packages':['bar','bar']});\n");
        htmlContent.append("google.charts.setOnLoadCallback(drawChart);\n");
        htmlContent.append("function drawChart() {\n");
        htmlContent.append("    var data = google.visualization.arrayToDataTable(dataRaw);\n");
        htmlContent.append("    var options = {\n");
        htmlContent.append("          title: 'TCC',\n");
        htmlContent.append("     };\n");
        htmlContent.append("    var chart = new google.visualization.Histogram(document.getElementById('ccChart'));\n");
        htmlContent.append("    chart.draw(data, options);\n");
        htmlContent.append("}\n");
        htmlContent.append("</script>\n");

        htmlContent.append("</body></html>");
        writeHtmlToFile(htmlContent.toString());
        System.out.println(" ccGlobale :" + membersWithoutGetter.getCcGlobale());
        System.out.println(" nbMethod :" + membersWithoutGetter.getNbMethod());
        System.out.println("moyenne :" + ((float) membersWithoutGetter.ccGlobale) / membersWithoutGetter.nbMethod);
    }


    private static void callTCC(File file) throws IOException {
        resultfile = new File("resultTCC.html");
        htmlContent = new StringBuilder("<html>" +
                "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\">\n" +
                "<script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>" +
                "</head>" +
                "<body><table>");

        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();
        TCC tcc = new TCC(htmlContent);

        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(tcc, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        htmlContent.append("</table></body></html>");
        htmlContent.append(
                "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "google.charts.load('current', {'packages':['bar']});\n" +
                "google.charts.setOnLoadCallback(drawChart);\n" +

                "console.log(dataRaw)"+
                "function drawChart() {\n" +
                "   var data = google.visualization.arrayToDataTable(dataRaw)\n" +

                " var options = {\n" +
                "          title: 'TCC',\n" +
                "          legend: { position: 'none' },\n" +
                "        };\n" +
                "\n" +
                "        var chart = new google.visualization.Histogram(document.getElementById('ccChart'));\n" +
                "        chart.draw(data, options);"+
                        "       }" +
                "</script>");
        writeHtmlToFile(htmlContent.toString());


    }
}
