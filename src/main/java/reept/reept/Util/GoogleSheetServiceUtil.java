//package com.ept.Util;


//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.services.sheets.v4.Sheets;
//import com.google.api.services.sheets.v4.SheetsScopes;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.api.client.json.jackson2.JacksonFactory;
//
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.List;

/*--------Old one----------*/
//public class GoogleSheetServiceUtil {
//	
//	public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
//        GoogleCredentials credentials = GoogleCredentials
//                .fromStream(new FileInputStream("src/main/resources/"))
//                .createScoped(List.of(SheetsScopes.SPREADSHEETS));
//
//        return new Sheets.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(),
//                new HttpCredentialsAdapter(credentials)
//        ).setApplicationName("Task Manager App").build();
//    }
//
//}

/*--------New one----------*/
//public class GoogleSheetServiceUtil {
//
//    public static Sheets getSheetsService(String credentialsFilePath) throws IOException, GeneralSecurityException {
//        GoogleCredentials credentials = GoogleCredentials
//                .fromStream(new FileInputStream(credentialsFilePath))
//                .createScoped(List.of(SheetsScopes.SPREADSHEETS));
//
//        return new Sheets.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(),
//                new HttpCredentialsAdapter(credentials)
//        ).setApplicationName("Task Manager App").build();
//    }
//}

package reept.reept.Util;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GoogleSheetServiceUtil {

//    public static Sheets getSheetsService(String credentialsFilePath) throws IOException, GeneralSecurityException {
//       GoogleCredentials credentials = GoogleCredentials
//                .fromStream(new FileInputStream(credentialsFilePath))
//                .createScoped(List.of(SheetsScopes.SPREADSHEETS));
//        
//  //      FileInputStream credentialsStream = new FileInputStream(credentialsFilePath); // absolute path like /etc/secrets/...       
////        	    GoogleCredentials credentials = GoogleCredentials
////        	            .fromStream(credentialsStream)
////        	            .createScoped(List.of(SheetsScopes.SPREADSHEETS));
//
//        HttpCredentialsAdapter baseInitializer = new HttpCredentialsAdapter(credentials);
//
//        // Wrap the base HttpRequestInitializer to add custom timeouts
//        HttpRequestInitializer timeoutInitializer = new HttpRequestInitializer() {
//            @Override
//            public void initialize(HttpRequest request) throws IOException {
//                baseInitializer.initialize(request);
//                request.setConnectTimeout(60000); // 60 seconds
//                request.setReadTimeout(60000);    // 60 seconds
//            }
//        };
//
//        return new Sheets.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(),
//                timeoutInitializer
//        ).setApplicationName("Task Manager App").build();
//    }
//    


	
	// public static Sheets getSheetsService(String credentialsFileName) throws IOException, GeneralSecurityException {
	//     InputStream credentialsStream = GoogleSheetServiceUtil.class
	//             .getClassLoader()
	//             .getResourceAsStream(credentialsFileName);

	//     if (credentialsStream == null) {
	//         throw new FileNotFoundException("Credentials file not found in classpath: " + credentialsFileName);
	//     }



		
//	    GoogleCredentials credentials = GoogleCredentials
//	            .fromStream(credentialsStream)
//	            .createScoped(List.of(SheetsScopes.SPREADSHEETS));

	
	    
	    // for tomcat 8.5
	//     GoogleCredentials credentials = GoogleCredentials
	//             .fromStream(credentialsStream)
	//             .createScoped(Arrays.asList(SheetsScopes.SPREADSHEETS));

		

	//     HttpCredentialsAdapter baseInitializer = new HttpCredentialsAdapter(credentials);

	//     HttpRequestInitializer timeoutInitializer = request -> {
	//         baseInitializer.initialize(request);
	//         request.setConnectTimeout(60000);
	//         request.setReadTimeout(60000);
	//     };

	//     return new Sheets.Builder(
	//             GoogleNetHttpTransport.newTrustedTransport(),
	//             JacksonFactory.getDefaultInstance(),
	//             timeoutInitializer
	//     ).setApplicationName("Task Manager App").build();
	// }

	public static Sheets getSheetsService(String credentialsFilePath) throws IOException, GeneralSecurityException {
    // Load directly from filesystem path (e.g. /etc/secrets/credentialsFirst.json)
    InputStream credentialsStream = new FileInputStream(credentialsFilePath);

    GoogleCredentials credentials = GoogleCredentials
            .fromStream(credentialsStream)
            .createScoped(List.of(SheetsScopes.SPREADSHEETS));

    HttpCredentialsAdapter baseInitializer = new HttpCredentialsAdapter(credentials);

    HttpRequestInitializer timeoutInitializer = request -> {
        baseInitializer.initialize(request);
        request.setConnectTimeout(60000);
        request.setReadTimeout(60000);
    };

    return new Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(),
            timeoutInitializer
    ).setApplicationName("Task Manager App").build();
}

}


