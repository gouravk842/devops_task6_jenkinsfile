job("task6_code_pull"){
        description("This will copy code from github & filter and send it to different directories present in the minikube based on file extension")
        scm {
                 github('gouravk842/devops_task3.git' , 'master')
             }
        triggers {
                scm("* * * * *")
                
  	}
        steps {
        shell('''sudo cp -rvf * /root/task3''')
      }
}


job("task6_html_webserver"){
        description("check the html server , launch it and deploy html file")
        
        triggers {
        upstream {
    upstreamProjects("task6_code_pull")
    threshold("Fail")
        }
        }
        steps {
        shell('''if sudo kubectl get pod | grep httpd-webserver
then
echo "httpd webserver already running"
else
sudo kubectl create -f /root/task3/httpd.yml
fi
''')
      }
}


job("task6_php_server"){
        description("check the php server , launch it and deploy the php file ")
        
        triggers {
                upstream {
    upstreamProjects("task6_html_webserver")
    threshold("Fail")
   } 
        }
        steps {
        shell('''if sudo kubectl get pods | grep php-webserver
then
echo "php-server already running fine"
else
sudo kubectl create -f /root/task3/php.yml
fi
''')
      }
}

job("task6_mailer"){
        description("email notification")
         triggers {
                upstream {
    upstreamProjects("task6_php_server")
    threshold("Fail")
   } 
         }
        steps {
        shell('''status=$(curl -o /dev/null -s -w "%{http_code}" http://192.168.99.104:32145/code.html)

echo $status
if [[ $status == 200 ]]
then
  echo "all good "
else 
 sudo python3 /root/msg.py
fi

status=$(curl -o /dev/null -s -w "%{http_code}" http://192.168.99.104:32146/code.php)

echo $status
if [[ $status == 200 ]]
then
  echo "all good "
else 
 sudo python3 /root/msg1.py
fi
''')
      }
}

job("task6_checker"){
        description("email notification")
         triggers {
                upstream {
    upstreamProjects("task6_code_pull")
    threshold("Fail")
   } 
         }
        steps {
        shell('''while true
do
if sudo kubectl get pod | grep php-webserver
then 
echo "all good"
else
if sudo kubectl get pvc | grep php-vol-pv 
then
sudo kubectl delete pvc php-vol-pv
fi
sudo kubectl create -f /root/task3/php.yml
fi

if sudo kubectl get pod | grep httpd-webserver
then 
echo "all good"
else
if sudo kubectl get pvc | grep httpd-pv-claim
then
sudo kubectl delete pvc httpd-pv-claim
fi
sudo kubectl create -f /root/task3/httpd.yml
fi

done

''')
      }
}

buildPipelineView('DevOps-task6-pipeline'){
    filterBuildQueue()
    filterExecutors()
    title('CI/CD Pipeline')
    displayedBuilds(3)
    selectedJob('task6_code_pull')
    alwaysAllowManualTrigger()
    showPipelineParameters()
    refreshFrequency(5)
}