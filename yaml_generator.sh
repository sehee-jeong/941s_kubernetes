#!/bin/bash

# ���� Ȯ��
if [ $# -lt 5 ]; then
  echo "����: $0 <�����ID> <���񽺸�> <�������> <�̹���> <��Ʈ1> [<��Ʈ2> <��Ʈ3>] [<ȯ�溯��1>=<��1> [<ȯ�溯��2>=<��2> ...]]"
  exit 1
fi

# ����� �Է� ���� ����
user_id=$1
service_name=$2
output_file=$3
image=$4
shift 4
ports=()
env_vars=()

# ��Ʈ ���� ó��
for ((i=1; i<=3; i++)); do
  if [ $# -gt 0 ]; then
    ports+=($1)
    shift
  fi
done

# ȯ�溯�� ���� ó��
while [ $# -gt 0 ]; do
  env_vars+=($1)
  shift
done

# ���� ��¥�� �ð��� ������� ���ϸ� ����
timestamp=$(date +"%Y%m%d%H%M%S")
output_file="${output_file}_${timestamp}.yaml"

# ���񽺸��� MD5 �ؽ÷� ��ȯ�Ͽ� ���ϸ� ����
service_name_md5=$(echo -n "$service_name" | md5sum | awk '{print $1}')
output_file="$service_name_md5.yaml"

# Namespace ����
namespace=$user_id

# YAML ���� �ۼ�
yaml_content=$(cat <<EOF
apiVersion: v1
kind: Namespace
metadata:
  name: $namespace
---
apiVersion: v1
kind: Service
metadata:
  name: $service_name-svc
  namespace: $namespace
spec:
  selector:
    app: $service_name
  type: LoadBalancer
  ports: \n
EOF
)

# ��Ʈ ���� �߰�
for port in "${ports[@]}"; do
  yaml_content+="  - name: $service_name-port-$port\n    protocol: TCP\n    port: $port\n    targetPort: $port\n"
done

yaml_content+="\n---\n"

yaml_content+="apiVersion: apps/v1
kind: Deployment
metadata:
  name: $service_name
  namespace: $namespace
spec:
  selector:
    matchLabels:
      app: $service_name
  template:
    metadata:
      labels:
        app: $service_name
    spec:
      containers:
      - name: $service_name
        image: $image
        ports:\n"

# ��Ʈ ���� �߰�
for port in "${ports[@]}"; do
  yaml_content+="        - containerPort: $port\n"
done

# ȯ�溯�� ���� �߰�
for var in "${env_vars[@]}"; do
  key="${var%=*}"
  value="${var#*=}"
  yaml_content+="        env:\n"
  yaml_content+="        - name: $key\n          value: $value\n"
done

# YAML ���� ����
echo -e "$yaml_content" > "$output_file"

echo "YAML ������ �����Ǿ����ϴ�: $output_file"
