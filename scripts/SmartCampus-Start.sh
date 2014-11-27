killall screen
echo 'Killing previous instances ...'
sleep 3
screen -S MessageQueue -d -m ./message-queue-script.sh
echo 'Starting Message Queue ...'
sleep 10
screen -S Collector -d -m ./collector-script.sh
echo 'Starting Collector ...'
screen -S DataProcessor -d -m ./data-processor-script.sh
echo 'Starting Messaging Processor ...'
screen -S DataAPI -d -m ./data-api-script.sh
echo 'Starting Data API ...'
screen -S ConfigAPI -d -m ./configAPI-script.sh
echo 'Starting Config API ...'