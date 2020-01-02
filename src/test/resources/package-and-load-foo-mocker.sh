mockServerClazzName=${1}
sandboxShDir=${2}
sandboxModuleDir=${3}
projectDir=${4}

jarName=sandbox-mocker-jar-with-dependencies.jar
mockerJsonName=foo-mocker.json


echo "mockServerClazzName:"${mockServerClazzName}
echo "sandboxShDir:"${sandboxShDir}
echo "sandboxModuleDir:"${sandboxModuleDir}
echo "projectDir:"${projectDir}


mvn clean compile package -DskipTests

# 防止sandbox用户模块目录没有被创建
mkdir -p ${sandboxModuleDir}

cp -r ${projectDir}/target/${jarName} ${sandboxModuleDir}
cp -r ${projectDir}/target/test-classes/${mockerJsonName} ${sandboxModuleDir}
cp -r ${projectDir}/target/test-classes/*.groovy ${sandboxModuleDir}

# 查看sandbox模块列表
ps -ef|grep ${mockServerClazzName} |awk ' NR ==1 {print $2 " -l" }' | xargs ${sandboxShDir}/sandbox.sh -p

# 卸载sandbox模块
ps -ef|grep ${mockServerClazzName} |awk ' NR ==1 {print $2 " -u 'mocker' " }' | xargs ${sandboxShDir}/sandbox.sh -p
# 刷新sandbox模块
ps -ef|grep ${mockServerClazzName} |awk ' NR ==1 {print $2 " -F " }' | xargs ${sandboxShDir}/sandbox.sh -p
# 加载sandbox模块
ps -ef|grep ${mockServerClazzName} |awk ' NR ==1 {print $2 " -d 'mocker/mock\?mock-config-path=${sandboxModuleDir}/${mockerJsonName}' " }' | xargs ${sandboxShDir}/sandbox.sh -p
