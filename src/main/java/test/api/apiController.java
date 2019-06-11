package test.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

//  localhost:8080/apiTest/apicontroller.do

@WebServlet("/apicontroller.do")
public class apiController extends HttpServlet{

	private static final long serialVersionUID = 1L;
	static final String API_KEY = "INSERT-PRIVATE-KEY";
	
	Summoner sum = new Summoner();
	String sumName;
	
	ArrayList<Boolean> winCondition = new ArrayList<Boolean>();
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		sumName = req.getParameter("name");
		//Processa os dados do Form pelo método POST
		try {
			findSummoner();
			matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Prepara os dados definidos em findSummoner() e matches() para a camada view em JSP
		req.setAttribute("sumName", sum.getName());
		req.setAttribute("sumIconId", sum.getIconId());
		req.setAttribute("winCondition", winCondition);
		
		RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/display.jsp");
		dispatcher.forward(req, resp);
	}
	
	//Método para realizar a conexao com a URL desejada conecta resposta JSON a string response
	private void ApiConnection(String url, StringBuffer response) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection)obj.openConnection();
		con.setRequestMethod("GET");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
	}
	
	//Método utilizado para achar o Jogador usando o Nome (Name) especificado no form da pagina HTML
	private void findSummoner() throws Exception{
		
		String url = "https://br1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + URLEncoder.encode(sumName, "UTF-8").replace("+", "%20")
				+ "?api_key=" + API_KEY;			
			
		StringBuffer response = new StringBuffer();
		ApiConnection(url, response);
		
		System.out.println(response.toString());

		// Leia a resposta JSON e imprima
		JSONObject myResponse = new JSONObject(response.toString());

		sum.setName(myResponse.getString("name"));
		sum.setIconId(myResponse.getInt("profileIconId"));
		sum.setAccountId(myResponse.getString("accountId"));
	}
	
	//Método que realiza a busca pelas 10 últimas partidas a partir do ID encontrado em findSummoner()
	private void matches() throws Exception{
		
		ArrayList<Long> match_id = new ArrayList<Long>();		
		
		String url_accountId = "https://br1.api.riotgames.com/lol/match/v4/matchlists/by-account/" + sum.getAccountId() + "?api_key=" + API_KEY;
				
		StringBuffer response = new StringBuffer();
		ApiConnection(url_accountId, response);

		System.out.println(response.toString());

		// Leia a resposta JSON e imprima
		JSONObject jsonObject = new JSONObject(response.toString());
		JSONArray matches = jsonObject.getJSONArray("matches");
		
		for (int i = 0; i < 10; ++i) {
			JSONObject matchInd = matches.getJSONObject(i);
			long matchh = matchInd.getLong("gameId");
			match_id.add(matchh);

			System.out.println(matchh + " " + "- Match ID");
		}
		
		
		// Acessando os dados de cada partida
		int y = 0;
		while(y <= 9)
		{
			String url_matchId = "https://br1.api.riotgames.com/lol/match/v4/matches/" + match_id.get(y) + "?api_key=" + API_KEY;
						
			StringBuffer response_matchId = new StringBuffer();
			ApiConnection(url_matchId, response_matchId);
			
			JSONObject jsonObjectMatchId = new JSONObject(response_matchId.toString());
			JSONArray participants = jsonObjectMatchId.getJSONArray("participantIdentities");
			
			boolean isFound = false;
			int j = 0;
			int participantIdIndex = 0;
			
			while(isFound == false){
				JSONObject part = participants.getJSONObject(j);
				JSONObject player = part.getJSONObject("player");

				String sumName = player.getString("summonerName");
				
				if(sumName.equals(this.sumName)){
					isFound = true;
					participantIdIndex = j;
				}
				++j;
			}

			JSONObject jsonObjectWin = new JSONObject(response_matchId.toString());
			JSONArray teams = jsonObjectWin.getJSONArray("participants");
			JSONObject winTeam = teams.getJSONObject(participantIdIndex);
			JSONObject stats = winTeam.getJSONObject("stats");
			boolean win = stats.getBoolean("win");
			winCondition.add(y, win);
			
			++y;
		}
	}	
}
