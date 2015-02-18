screen -X -S MessageQueue kill
screen -X -S DataProcessor kill
screen -X -S DataAPI kill

echo 'Killing previous instances ...'
sleep 3
screen -S MessageQueue -d -m ./message-queue-script.sh
echo 'Starting Message Queue ...'
sleep 10
screen -S DataProcessor -d -m ./data-processor-script.sh
echo 'Starting Messaging Processor ...'
screen -S DataAPI -d -m ./data-api-script.sh
echo 'Starting Data API ...'
sleep 5
