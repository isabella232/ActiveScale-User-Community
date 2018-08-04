/* 
Copyright (c) 2018 Western Digital Corporation or its affiliates.
SPDX-License-Identifier: MIT
Description/Other notes
 * Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
=====================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
*/
package com.amazonaws.samples;

import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;

public class S3Sample {

	static Scanner userInput = new Scanner(System.in);
    static String bucketName = "*Bucket name*";
    static String fileObjKeyName = "*txtdocument.txt*";
    static String fileName = "*Path to file to upload*";
	static AmazonS3 s3Client;
	
    public static void main(String[] args) {
    try {
    	BasicAWSCredentials credentials = new BasicAWSCredentials("*access_key_id*", "*secret_key_id*");
        EndpointConfiguration endpoint = new EndpointConfiguration("*endpoint*", bucketName);
    	s3Client = AmazonS3ClientBuilder.standard()
    			.withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(endpoint)
            .build();
    	List<Bucket> list = s3Client.listBuckets();
    }
    catch(Exception e) 
    {
		e.printStackTrace();
		System.out.print("Call was made correctly but unable to access account");
    }

    while (true){
		System.out.println("Would you like to (1) Read or (2) Write?");
		System.out.println("Type in 1 for for read or 2 for write");
			int response = userInput.nextInt();
			if (response == 1)
			{
				fileRetriever("./*txtdocument.txt*");

				System.exit(0);
			}
			else if (response == 2)
			{
				systemWriter();
				System.exit(0);
			}
			else
			{
				System.out.println("Invalid Input. Please try again");
			}
		}
	}
    
	
	public static void systemWriter() 
	{
        PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, new File(fileName));
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("modified document");
        metadata.addUserMetadata("x-amz-meta-title", "someTitle");
        request.setMetadata(metadata);
        s3Client.putObject(request);
	}
	
    
    public static void fileRetriever(String nameofFile) {

        System.out.println("Downloading an object");
        S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, fileObjKeyName));
        System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
        System.out.println("Content: ");
        try {
			displayTextInputStream(s3object.getObjectContent());
		} catch (IOException e) {
			e.printStackTrace();
		}       
	}
	
	   private static void displayTextInputStream(InputStream input) throws IOException {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            System.out.println(line);
	        }
	        System.out.println();
	    }

}