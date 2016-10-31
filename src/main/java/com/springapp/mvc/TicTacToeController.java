package com.springapp.mvc;

import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/")
@Scope("singleton")
public class TicTacToeController {

	private final static int SIZE = 3;
	private final static String ERROR_MESSAGE_NOT_EMPTY = "<script>alert('You have made choice in a filled cell!');</script>";
	private final static String MESSAGE_YOU_WON = "<script>alert('Congratulations! You won!!!');</script>";
	private final static String MESSAGE_YOU_LOST = "<script>alert('Unfortunately, you lost (((');</script>";
	private final static String MESSAGE_MOVES_ENDED = "<script>alert('Moves ended!');</script>";


	HttpSession session = null;

	@RequestMapping("/")
	public String resetFunction(ModelMap model, HttpServletRequest request) {
		setInitialModel(request, model);
		request.getSession().removeAttribute("movesUserSession");
		return "tictactoe";
	}

	@RequestMapping(value = "move", method = RequestMethod.POST)
	public String makeMoves(ModelMap model, HttpServletRequest request){

		//Создание сессии
		session = request.getSession(true);

		//установка данных модели путём считывания из request без учёта нового хода
		setModelAttributesWithoutMoves(model, request);

		//Создание коллекции Map и запись в неё значений, находящихся в клетках, без учёта нового хода
		Map movesMassive =  fillMassive(request);

		//считывание клетки, в которой был сделан ход
		String userMove = request.getParameter("but");
		//разбиение хода типа String на массив int для последующего применения
		int[] userMoveInt = conversionStringToInt(userMove);

		model.addAttribute("movesUserModel", readSession("movesUserSession"));
		model.addAttribute("movesCompModel", readSession("movesCompSession"));

		//проверка на ход в заполненную клетку
		if(getSteps(userMove, movesMassive).equals("empty")) {

			//Создание коллекции Map и запись в неё значений, находящихся в клетках, с учётом хода пользователя
			Map movesMassWithUserStep =  movesMassive;

			movesMassWithUserStep.put(userMove, "x");

			//Создание описания хода пользователя с занесением в сессию для вывода в jsp
			makeUserStepDescription(userMoveInt, session, model);

			//Установка значений модели в зависимости от хода пользователя
			setModelAttributesWithUserMove(userMove, model, request);

			//Проверка на победу пользователя
			if(!(winningCheck(movesMassWithUserStep, "x") == 1)) {

				//проверка на существование ходов
				if(movesMassWithUserStep.containsValue("empty")) {
					//Рандомный ход компьютера с ограничениями
					String compMove = computerMakeMove(userMove, movesMassive);

					//разбиение хода типа String на массив int для последующего применения
					int[] compMoveInt = conversionStringToInt(compMove);

					//Создание описания хода компьютера с занесением в сессию для вывода в jsp
					makeCompStepDescription(compMoveInt, session, model);

					//Установка значений модели в зависимости от хода компьютера
					setModelAttributesWithCompMove(compMove, model, request);

					//Создание коллекции Map и запись в неё значений, находящихся в клетках, с учётом ходов пользователя и компьютера
					Map movesMassWithCompStep =  movesMassWithUserStep;
					movesMassWithCompStep.put(compMove, "o");

					//Проверка на победу компьютера
					if (winningCheck(movesMassWithCompStep, "o") == 1) {
						model.addAttribute("newMessage", MESSAGE_YOU_LOST);
						setInitialModel(request, model);
						session.removeAttribute("movesUserSession");
					}

					//System.out.println(movesMassive.toString());
				}
				else{
					model.addAttribute("newMessage", MESSAGE_MOVES_ENDED);
					setInitialModel(request, model);
					session.removeAttribute("movesUserSession");
				}
			} else {
				model.addAttribute("newMessage", MESSAGE_YOU_WON);
				setInitialModel(request, model);
				session.removeAttribute("movesUserSession");
			}
		} else {
			model.addAttribute("newMessage", ERROR_MESSAGE_NOT_EMPTY);
		}
		return "tictactoe";
	}

	public void makeUserStepDescription(int[] userMoveInt, HttpSession session, ModelMap model){
		String strMovesUser = readSession("movesUserSession");
		String strTemplateUser = "You have made a choice in the cell number " + userMoveInt[0] + ":" + userMoveInt[1] + "<BR/>";
		System.out.println(strTemplateUser);

		strMovesUser += strTemplateUser;
		if (strMovesUser.contains("null")) {
			strMovesUser = strMovesUser.split("null")[1];
		}

		session.setAttribute("movesUserSession", strMovesUser);
		model.addAttribute("movesUserModel", strMovesUser);
	}

	public void makeCompStepDescription(int[] compMoveInt, HttpSession session, ModelMap model){
		String strMovesComp = readSession("movesCompSession");
		String strTemplateComp = "Computer has made a choice in the cell number " + compMoveInt[0] + ":" + compMoveInt[1] + "<BR/>";
		System.out.println(strTemplateComp);

		strMovesComp += strTemplateComp;
		if (strMovesComp.contains("null")) {
			strMovesComp = strMovesComp.split("null")[1];
		}

		session.setAttribute("movesCompSession", strMovesComp);
		model.addAttribute("movesCompModel", strMovesComp);
	}

	public String randomMove(){
		int i = (new Random().nextInt(3)) + 1;
		int j = (new Random().nextInt(3)) + 1;
		String str = "" + i + j;
		//System.out.println(str);
		return str;
	}

	public int winningCheck(Map map, String str){
		int a = 0;
		if((map.get("11").equals(str) & map.get("12").equals(str) & map.get("13").equals(str)) ||
				(map.get("11").equals(str) & map.get("21").equals(str) & map.get("31").equals(str)) ||
				(map.get("11").equals(str) & map.get("22").equals(str) & map.get("33").equals(str)) ||
				(map.get("12").equals(str) & map.get("22").equals(str) & map.get("32").equals(str)) ||
				(map.get("13").equals(str) & map.get("23").equals(str) & map.get("33").equals(str)) ||
				(map.get("13").equals(str) & map.get("22").equals(str) & map.get("31").equals(str)) ||
				(map.get("21").equals(str) & map.get("22").equals(str) & map.get("23").equals(str)) ||
				(map.get("31").equals(str) & map.get("32").equals(str) & map.get("33").equals(str)))
			a = 1;
		return a;
	}

	public Map fillMassive(HttpServletRequest request){
		Map map = new HashMap();
		for(int i = 1; i <= SIZE; i++){
			for(int j = 1; j <= SIZE; j++){
				map.put("" + i + j, request.getParameter("" + i + j));
			}
		}
		return map;
	}

	public String readSession(String param){
		String sessionData = (String)session.getAttribute(param);
		return sessionData;
	}

	public int[] conversionStringToInt(String str){
		int[] userMoveInt = {Character.getNumericValue(str.charAt(0)), Character.getNumericValue(str.charAt(1))};
		return userMoveInt;
	}

	public void setModelAttributesWithUserMove(String userMove, ModelMap model, HttpServletRequest request){
		request.setAttribute(userMove, "x");
		model.addAttribute("image" + userMove, request.getAttribute(userMove));
	}

	public void setModelAttributesWithCompMove(String compMove, ModelMap model, HttpServletRequest request){
		request.setAttribute(compMove, "o");
		model.addAttribute("image" + compMove, request.getAttribute(compMove));
	}

	public String computerMakeMove(String userMove, Map map){
		String compMove = "";

		while(compMove.equals("") || compMove.equals(userMove) || !getSteps(compMove, map).equals("empty")){
			compMove = randomMove();
		}
		return compMove;
	}

	public String getSteps(String str, Map map){
		String value = (String)map.get(str);
		return value;
	}

	public void setModelAttributesWithoutMoves(ModelMap model, HttpServletRequest request){
		request.setAttribute("11", request.getParameter("11"));
		request.setAttribute("12", request.getParameter("12"));
		request.setAttribute("13", request.getParameter("13"));
		request.setAttribute("21", request.getParameter("21"));
		request.setAttribute("22", request.getParameter("22"));
		request.setAttribute("23", request.getParameter("23"));
		request.setAttribute("31", request.getParameter("31"));
		request.setAttribute("32", request.getParameter("32"));
		request.setAttribute("33", request.getParameter("33"));
		model.addAttribute("image11", request.getAttribute("11"));
		model.addAttribute("image12", request.getAttribute("12"));
		model.addAttribute("image13", request.getAttribute("13"));
		model.addAttribute("image21", request.getAttribute("21"));
		model.addAttribute("image22", request.getAttribute("22"));
		model.addAttribute("image23", request.getAttribute("23"));
		model.addAttribute("image31", request.getAttribute("31"));
		model.addAttribute("image32", request.getAttribute("32"));
		model.addAttribute("image33", request.getAttribute("33"));
	}

	public void setInitialModel(HttpServletRequest request, ModelMap model){
		request.setAttribute("image11", "empty");
		request.setAttribute("image12", "empty");
		request.setAttribute("image13", "empty");
		request.setAttribute("image21", "empty");
		request.setAttribute("image22", "empty");
		request.setAttribute("image23", "empty");
		request.setAttribute("image31", "empty");
		request.setAttribute("image32", "empty");
		request.setAttribute("image33", "empty");
		model.addAttribute("image11", request.getAttribute("image11"));
		model.addAttribute("image12", request.getAttribute("image12"));
		model.addAttribute("image13", request.getAttribute("image13"));
		model.addAttribute("image21", request.getAttribute("image21"));
		model.addAttribute("image22", request.getAttribute("image22"));
		model.addAttribute("image23", request.getAttribute("image23"));
		model.addAttribute("image31", request.getAttribute("image31"));
		model.addAttribute("image32", request.getAttribute("image32"));
		model.addAttribute("image33", request.getAttribute("image33"));
		model.remove("movesUserModel");
		model.remove("movesCompModel");
	}
}