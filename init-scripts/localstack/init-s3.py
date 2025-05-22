from dotenv import dotenv_values
import boto3

env_path = "/etc/localstack/init/ready.d/.env"
config = dotenv_values(env_path)

S3_KEY = config.get("S3_KEY")
S3_SECRET = config.get("S3_SECRET")
S3_URI = config.get("S3_URI")
S3_RESOURCE_BUCKET_NAME = config.get("S3_RESOURCE_BUCKET_NAME")

print(f"S3_KEY: {S3_KEY}")
print(f"S3_SECRET: {S3_SECRET}")
print(f"S3_URI: {S3_URI}")
print(f"S3_RESOURCE_BUCKET_NAME: {S3_RESOURCE_BUCKET_NAME}")

s3_client = boto3.client(
    "s3",
    endpoint_url=S3_URI,
    aws_access_key_id=S3_KEY,
    aws_secret_access_key=S3_SECRET
)

try:
    s3_client.create_bucket(Bucket=S3_RESOURCE_BUCKET_NAME)
    print("Bucket 'resources' created successfully.")
except Exception as e:
    print(f"Error creating bucket: {e}")