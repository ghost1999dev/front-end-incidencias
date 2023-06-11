package com.android.proyectoincidencias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class AdminActivity extends AppCompatActivity {

    Button registerUser,registerIncidence,generarPdf;
    private static final String API="http://192.168.76.44/api-incidencias/api-listar-incidencias.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        registerUser=findViewById(R.id.registerUser);
        registerIncidence=findViewById(R.id.registerIncidence);
        generarPdf=findViewById(R.id.generarPdf);

        generarPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatosYGenerarPDF();
            }
        });


        registerIncidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this,IncidenceRegister.class);
                startActivity(intent);
            }
        });

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, UserRegister.class);
                startActivity(intent);
            }
        });
    }



    private void obtenerDatosYGenerarPDF() {
        StringRequest request = new StringRequest(Request.Method.POST, API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
                    if (status.equals("success")) {
                        String jsonData=jsonArray.toString();
                        //System.out.println(jsonData);
                        generarPdf(jsonData);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }



    private void generarPdf(String datos){
        try {

            String filename = System.currentTimeMillis()+".pdf";
            File pdfFile= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),filename);
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            Document document=new Document();
            PdfWriter.getInstance(document,outputStream);
            Uri pdfUri = FileProvider.getUriForFile(AdminActivity.this, BuildConfig.APPLICATION_ID+".provider",pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri,"application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            document.open();

            Paragraph titulo = new Paragraph("REPORTE DE INICIDENCIAS");
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD,18, Font.BOLD));
            document.add(titulo);
            Paragraph espacio = new Paragraph(" "); // Párrafo vacío
            document.add(espacio);

            PdfPTable table= new PdfPTable(5);

            table.addCell("ID");
            table.addCell("DESCRIPCION");
            table.addCell("FECHA");
            table.addCell("ESTADO");
            table.addCell("USUARIO");

            JSONArray jsonArray = new JSONArray(datos);
            try {
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String columna1 = jsonObject.getString("id");
                    String columna2 = jsonObject.getString("descripcion");
                    String columna3 = jsonObject.getString("fecha");
                    String columna4 = jsonObject.getString("estado");
                    String columna5 = jsonObject.getString("nombre");

                    table.addCell(columna1);
                    table.addCell(columna2);
                    table.addCell(columna3);
                    table.addCell(columna4);
                    table.addCell(columna5);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            document.add(table);
            //document.add(new Paragraph(datos));
            document.close();
            Toast.makeText(AdminActivity.this, "PDF EXPORTADO SATISFACTORIAMENTE", Toast.LENGTH_LONG).show();
        }catch (FileNotFoundException | DocumentException | JSONException e){
            e.printStackTrace();
        }
    }


}