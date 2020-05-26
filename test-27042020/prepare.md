# Questões - Sistemas Distribuídos

## 0  - Desenha a arquitetura deste sistema distribuído.



## 1  - Diferença entre sistema distribuído síncrono e assíncrono.

- Num sistema síncrono podem existir limites máximos de tempo conhecido para:
	- Tempos de execução de processos;
		- Atrasos na comunicação;
		- Variação;
		- O tempo necessário para executar cada passado de um processo tem um limite inferior e um limite superior conhecidos;
		- Cada mensagem transmitida por um canal é recebida dentro de um limite de tempo conhecido;
		- Cada processo tem um relógio cujo desvio máximo para o tempo de referência é conhecido.



## 2  - Neste caso qual a melhor maneira de conciliar diferentes timelines?

- Tempo Global, implementa a abstração de um tempo universal, através de um relógio que fornece o mesmo tempo a todos os participantes no sistema;
- Tempo Absoluto, padrões universalmente ajustados, disponíveis como fontes de tempo externo para o qual qualquer relógio interno se pode sincronizar;
- Relógio físico local, o modo mais comum para fornecer uma fonte de tempo num processo.



## 3  - Sabendo que um processo a precede um processo b, justifique o porquê de T(a) < T(b).

- Se "a" e "b" são eventos no mesmo processo e "a" ocorre antes de "b" então dizemos que "a" precede "b".
- Tal como sendo "a" o envio da mensagem m e "b" é a receção da mensagem então "a" precede "b"
- Com isto em mente, se temos um processo "a" que precede o processo "b" então T(a) < T(b)



## 4  - Quais os tipos de modelos cliente/servidor e quais as suas vantagens/desvantagens, no que diz respeito a múltiplos servidores?

- Cliente/Servidor replicado
	- Vantagens:
  		- Permite distribuir a carga, melhorando o desempenho;
  		- Não existe um ponto de falha único.
  	- Problema:
  		- Manter o estado do servidor coerente em todas as réplicas.
      
- Cliente/Servidor particionado
	- Vantagens:
  		- Permite distribuir a carga, melhorando o desempenho  
		- Não existe um ponto de falha único
	
	- Problema:
		- Falha de um servidor impede acesso aos dados presentes nesse servidor



## 5  - O que é a arquitetura de um sistema distribuído?

- A arquitetura de um sistema distribuído é a estrutura do sistema em termos de \textbf{localização} das suas diferentes partes, do \textbf{papel} que cada parte desempenha e como se interrelacionam.
- A arquitetura tem implicações no \textbf{desempenho, fiabilidade e segurança do sistema}.



## 6  - Em quantos aspetos transversais podem ser avaliados todos os sistemas?

- Os sistemas podem ser avaliados nos seguintes aspetos : 
	- Modelo de interação (ou de sincronismo).	
	- Modelo de falhas (ou avarias).
	- Modelo de segurança.


 
## 7  - Quais os aspetos cujo o tempo é usado num sistema distribuído?

- O uso do tempo em sistemas distribuídos é feito em dois aspetos:
	- Registar e observar a localização de eventos na timeline;
	- Forçar o futuro posicionamento de eventos na timeline;



## 8  - No contexto da tecnologia Java Enterprise Edition, explique a diferença entre um “stateful session bean” e um “stateless session bean”.

- Stateful session Bean : 
	- Cada instancia de um Stateful bean está a ssociado a um unico cliente.
	- O bean mantém um estado de sessão com um dado cliente.
	- Guarda informação
	- Têm tempo de vida

- Stateless Session Beans:
	- Não mantém informação especifica de um cliente.
	- O estado existe apenas durante a invocação de um metodo.
	- O servidor gere uma pool de instncias que servem pedidos de vários clientes;
	- Interface pode ser exposta como web service.



## 9  - Explique como funcionam os relógios lógicos de Lamport para atribuir um timestamp a uma sequência de eventos num sistema distribuído.

- Com base nos relógios lógicos de Lamport, quando uma mensagem é enviada de um processo para outro é também enviado o tempo do processo emissor.
- Quando é necessário atribuir um timestamp a um determinado evento, este antes vai ter de ser incrementado e, após o sucedido, envia a mensagem para outro processo.
- O processor recetor, irá efetuar o mesmo passo anterior para atribuir o timestamp ao evento de receção de mensagem.



## 10 - Descreva como pode sincronizar os relógios de duas máquinas usando um algoritmo de sincronização interna.

- Algoritmo de Berkeley (sincronização interna)
	- É escolhido um computador para ser o co-ordenador (master)
	- O master periodicamente contacta os outros computadores, (slaves)
	- O master faz uma estimativa do tempo local de cada slave, baseado no rtt.
	- O master calcula o tempo médio de todos os computadores, ignorando valores de transmissão demasiado elevados e máquinas com tempos muito diferentes dos outros
	- Finalmente o master envia a cada computador o valor de que o seu relógio deve ser ajustado (esse valor pode ser positivo ou negativo)  



## 11 - Considerando o problema de ordenação de eventos distribuídos, diga qual a diferença entre uma ordenação FIFO e uma ordenação causal e como podem ser implementadas.

- FIFO:
	- No caso do FIFO( Fist In First Out) , quaisquer duas mensagens enviadas pelo mesmo processo, são entregues pela ordem de envio a qualquer outro processo.
	- Esta ordem no FIFO é assegurada pela atribuição de um número de sequencia local.
	- O receptor entrega as mensagens à aplicação pela ordem dos seus números de sequência.
	- Para isso o receptor deve ter um buffer temporário para as mensagens recebidas fora de ordem e caso exista alguma em falta pedir a retransmissão da mesma.

- Ordenação Causal:
	- Consiste na garantia de que as mensagens enviadas por processos diferentes são entregues pela “ordem correta” no receptor.
	- Neste caso a informação sobre a sequencia lógica dos eventos é incluida nas mensagens.



## 12 - No seu trabalho prático 2 que está a desenvolver, tem classes java que são entities, outras que são stateless session beans, e outras que são named beans. Tem ainda páginas em XHTML. Descreva qual o papel de cada um destes componentes e como se interligam na sua aplicação


## 13 - Exercícios práticos.

