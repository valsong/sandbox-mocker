mockServerClazzName=MockFooTest
sandboxShDir=${HOME}/.opt/sandbox/bin
sandboxModuleDir=${HOME}/.sandbox-module

mockerJsonName=foo-mocker.json


echo "mockServerClazzName:"${mockServerClazzName}
echo "sandboxShDir:"${sandboxShDir}
echo "sandboxModuleDir:"${sandboxModuleDir}
echo "projectDir:"${projectDir}

ps -ef|grep ${mockServerClazzName} |awk ' NR ==1 {print $2 " -l" }' | xargs ${sandboxShDir}/sandbox.sh -p
ps -ef|grep ${mockServerClazzName} |awk ' NR ==1 {print $2 " -u 'mocker' " }' | xargs ${sandboxShDir}/sandbox.sh -p
ps -ef|grep ${mockServerClazzName} |awk ' NR ==1 {print $2 " -F " }' | xargs ${sandboxShDir}/sandbox.sh -p
ps -ef|grep ${mockServerClazzName} |awk ' NR ==1 {print $2 " -d 'mocker/mock\?mock-config-path=${sandboxModuleDir}/${mockerJsonName}' " }' | xargs ${sandboxShDir}/sandbox.sh -p
