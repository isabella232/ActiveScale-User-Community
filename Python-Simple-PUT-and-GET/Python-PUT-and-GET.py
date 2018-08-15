'''
Copyright (c) 2018 Western Digital Corporation or its affiliates.
SPDX-License-Identifier: MIT
This code is based on code with license:
=====================================================================================
https://boto3.readthedocs.io/en/latest/guide/s3-example-creating-buckets.html
* Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
'''

import boto3
import botocore

client = boto3.client('s3')
session = boto3.session.Session()

#modify the fields with the astericks
s3_client = session.client(
    service_name='s3',
    aws_access_key_id='*access_key*',
    aws_secret_access_key = '*secret_key*',
    endpoint_url='*endpoint_url*',
)

print("Would you like to do a simple PUT or GET on a text file?")
choice = input("To do a PUT, enter 1. To do a GET, enter 2 : ")

if choice == "1":
    question = input("Would you like to make a new bucket? ")

    if question == "yes":
        bucketname = input("Type in the name of your new Bucket: ")
        try:
            s3_client.head_bucket(bucketname)
        except:
            print("This bucket already exists.")

        s3_client.create_bucket(Bucket= bucketname)
        filename = input("Enter the name of the file you want to upload (ex: kat.txt) : ")
        s3_client.upload_file(filename, bucketname, filename)
        print("Done!")


    if question == "no":
        filename = input("Enter the name of the file you want to upload (ex: kat.txt) : ")
        bucketname = input("Enter name of bucket you would like to store this file in: ")
        s3_client.upload_file(filename, bucketname, filename)
        print("Done!")

if choice == "2":

    response = s3_client.list_buckets()
    buckets = [bucket['Name'] for bucket in response['Buckets']]
    print("Bucket List: %s" % buckets)

    select = input("Select a Bucket you'd like to read from: ")
    filename = input("Enter the name of the file you would like to access: ")

    try:
        with open (filename, 'wb') as data:
            s3_client.download_fileobj(select,filename,data)

    except botocore.exceptions.ClientError as e:
        if e.response['Error']['Code'] == "404":
            print("The object does not exist.")
        else:
            raise


