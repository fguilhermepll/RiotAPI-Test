# RiotAPI-Test
Projeto realizado com o intuito te aprender mais sobre como consumir dados a partir de uma API, entender mais sobre o ciclo de vida de uma aplicação web MVC

# Funcionamento do projeto

A tarefa do projeto é usar a API desenvolvida pela empresa Riot Games para retornar as últimas 10 partidas de tal jogador - também definido como "Summoner" -
e mostrar quais jogos foram obtidos vitória e em quais foram derrota.

O projeto se inicia pela URL ".../apiTest/form.html", que é apenas um pequeno <form> com método 'POST' e transfere o dado consumido para ser
processado em apiController

![](https://i.imgur.com/N1b4sWL.png)<br>
(Entrada de dado é o nome ou "nick" de determinado jogador/summoner)

Após o clicar em "Send" o nome definido será passado como request pelo parâmetro 'POST' e processado pelo servlet "apiController",
executando queries e fazendo requests à API, que tem retorno em formato JSON, e após todo seu processamento, irá retornar o "nick" do jogador
e as suas últimas 10 partidas jogadas, identificando quais foram vitória e quais foram derrota.

![](https://i.imgur.com/4rzBF3X.png)<br>

("UP Hy0g4" = nick, e os próximos dados são as 10 últimas partidas)

# Aprendizado

Foi um projeto mais complicado pro meu nível, League Of Legends é um jogo que eu tinha conhecimento e descobri sobre a parte da comunidade de desenvolvedor
que a empresa dava suporte após um tempo. Já tinha interesse em tentar aprender a consumir dados externos de uma API e "interpretar" dados vindos de formato JSON em Java, então senti que essa seria uma boa oportunidade.

Consegui entender um pouco melhor como funcionam as páginas JSP (JavaServer Pages), como é o ciclo de vida do Servlet e como o Cliente se 
comunica com o Servidor atráves dos objetos Request e Response, gerenciados pelo TomCat.
