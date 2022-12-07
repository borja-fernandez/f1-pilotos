package com.f1.pilots.rabbitMQ;

import com.f1.pilots.rabbitMQ.model.PilotoRabbit;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class RabbitMQSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private Queue queue;
    public void send(PilotoRabbit pilot) {
        rabbitTemplate.convertAndSend(queue.getName(), pilot);
    }
}
