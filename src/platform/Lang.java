/*
 * Created on 11/09/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package platform;

import javax.swing.JOptionPane;

import util.Define;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Lang implements Define {

	public static void setDefaultLanguage ( String parLanguage) {
		switch ( parLanguage.charAt ( 0)) {
			case 'e':
			case 'E':
				DefaultLanguage = ENGLISH;
				break;	

			default:
				DefaultLanguage = PORTUGUESE;
				break;
		}
	}

	public static int getDefaultLanguage ( ) {
		return ( DefaultLanguage);
	}

	public static void main(String[] args) {
	}
	
	public static int iLang;
	public static String msgsGUI [] = {
			"T&D-Bench Simulator",
			"Neander Processor",
			"Datapath",
			"Block Diagram",
			"Control",
			"Pipeline and Instructions",
			"Datapath (2)",
			"Components",
			"Messages",
			"Messages to the User",
			"File",
			"Load program...",
			"Reset",
			"Exit",
			"View",
			"Decimal",
			"Hexa",
			"Binary",
			"Tracking",
			"Simulate",
			"Simulate...",
			"Steering",			
			"Set port value...",
			"Set contents...",
			"Execute...",//[24]
			" Cycle",
			" Instruction",
			"Set processor...",
			"Instruction Queues",
			"Processor Status",
			"Simulador do T&D-Bench",//[30]
			"Processador Neander",
			"Caminho de Dados",
			"Diagrama de Blocos",
			"Controle",
			"Pipeline e Instruções",
			"Caminho de Dados (2)",
			"Componentes",
			"Mensagens",
			"Mensagens ao usuário",
			"Arquivo",
			"Abrir programa...",
			"Reset",
			"Sair",
			"Exibir",
			"Decimal",
			"Hexa",
			"Binário",
			"Simular",
			"Simular",
			"Simular...",
			"Alterar",
			"Alterar valor de porta...",
			"Alterar conteúdo...",
			"Executar...",
			" Ciclos",
			" Instruções",
			"Configurar processador...",
			"Filas de Instruções",
			"Status do Processador",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"Choose the component:      ",//[70]
			"COMPONENT PORTS",
			"",
			"",
			"",
			"Escolha o componente:      ",//[75]
			"Portas",
			"",
			"",
			"",
			"Choose the instruction queue:      ",//[80]
			"DETAILS OF THE INSTRUCTIONS IN QUEUE ",
			"DETAILS OF THE INSTRUCTIONS IN EXECUTION",
			"",
			"",
			"Escolha a fila de instruções:      ",//[85]
			"Detalhes da instrução na fila",
			"Detalhes da instrução em execução",
			"",
			"",
			" CONTENTS",//[90]
			"Address",
			"Contents",
			"Mnem",
			"Index",
			" CONTEÚDO",//[95]
			"Endereço",
			"Conteúdo",
			"Mnemônico",
			"Índice",
			"INSTRUCTIONS IN THE PIPELINE",//[100]
			"",
			"",
			"",
			"",
			"Instruções em execução (ciclo a ciclo)",//[105]
			"",
			"",
			"",
			"",
			" PROCESSOR STATUS ",//[110]
			"",
			"",
			"",
			"",
			" Estado do processador ",//[115]
			"",
			"",
			"",
			"",
			"COMPONENT CONTENTS",//[120]
			"Index",
			"Value",
			"",
			"",
			"Conteúdo do componente",//[125]
			"Posição",
			"Valor",
			"",
			"",
			"Choose the execution path:      ",//[130]
			"",
			"",
			"",
			"",
			"Escolha o caminho de execução   ",//[135]
			"",
			"",
			"",
			"",
			"Component",//[140]
			"Method",
			"",
			"",
			"",
			"Componente",//[145]
			"Método",
			"",
			"",
			"",
			"Component",//[150]
			"Port type",
			"Selecionado",
			"Value",
			"Attributes",
			"Componente",//[155]
			"Tipo de porta",
			"Selecionado",
			"Valor",
			"Atributos",
			"STATUS or CONFIG.",//[160]
			"FIELD",
			"STRING",
			"",
			"",
			"STATUS ou CONFIG.",//[165]
			"CAMPO NUMÉRICO",
			"CAMPO STRING",
			"",
			"",
			"Time Units: ",//[170]
			"",
			"",
			"",
			"",
			"Unidades de tempo",//[175]
			"",
			"",
			"",
			"",
			"T&D-Bench Error",//[180]
			"ERROR",
			"Invalid index: ",
			"Conversion Error",
			"",
			"",//[185]
			"",
			"",
			"",
			"",
			"Erro na Simulação",//[190]
			"ERRO",
			"�ndice inv�lido: ",
			"Erro na conversão",
			"",
			"",//[195]
			"",
			"",
			"",
			"",
			"Help",//[200]
			"Usage tips",
			"No Help",
			"List rISAs",
			"List references by instruction address",
			"Ajuda",//[205]
			"Dicas de Uso",
			"Ajuda indisponível",
			"Listar rISAs",
			"Listar referências a instruções - por endereço",
			"Exit",//[210]
			"",
			"",
			"",
			"",
			"Sair",//[215]
			"",
			"",
			"",
			"",
			"List rISA bit fields size",//[220]
			"",
			"",
			"",
			"",
			"Listar tamanho de campos de instr.rISA",//[225]
			"",
			"",
			"",
			"",
			"",//[230]
			"",
			"",
			"",
			"",
			"LC-3 Processor",//[235]
			"",
			"",
			"",
			"",
			"",//[240]
			"",
			"",
			"",
			"",
			"",//[245]
			"",
			"",
			"",
			"",
			"",//[250]
			"",
			"",
			"",
			"",
			"Processador LC-3",//[255]
			"",
			"",
			"",
			"",
			"",//[260]
			"",
			"",
			"",
			"",
			"",//[265]
			"",
			"",
			"",
			"",
			"",//[270]
			"",
			"",
			"",
			"",
			"BUBBLE",//[275]
			"Simulate and list time execution",
			"Set breakpoint...",
			"Breakpoints",
			" BREAKPOINTS ",
			"Nenhuma Operação",//[280]
			"Simular e mostrar o tempo de simulação",
			"Definir ponto de parada...",
			"Pontos de parada",
			" PONTOS DE PARADA ",
			"Simular com rISA...", // [285]
			"Simulate with rISA...",
			"Simular com rISA",
			"Simulate with rISA",
			"Escolha um conjunto de intruções reduzidas predefinido:", 
			"Choose a predefined reduced instruction set:", //[290]
			"Sem rISA",
			"Without rISA",
			"Tratamento de saltos",
			"Tamanho pequeno dos blocos",
			"Número de palavras buscadas",
			"Ciclos de execução",
			"",
			"rISA configurável",
			"User defined rISA",
			"Configurar rISA", //[300]
			"Define a reduced intruction set",
			"Gerar gráfico",
			"Create graphic",
			"Configurar gráfico",
			"Graphic settings",
			"Configuração rISA",
			"rISA settings",
			"Escolha o formato das instruções reduzidas:",
			"Choose the format of the reduced intructions:",
			"", //[310]
			"",
			"Escolha as 8 instruções que serão reduzidas:",
			"Choose the 8 instructions to be reduced:",
			"Escolha as 16 instruções que serão reduzidas:",
			"Choose the 16 instructions to be reduced:",
			"Limpar",
			"Clear",
			// Classe GraphicDialog:
			"Escolha as informações que serão exibidas no gráfico:",
			"Choose the information that will be shown in the graphic:",
			"", //[320]
			"Jump Handling",
			"Small size of the blocks",
			"Number of words fetched",
			"Execution Cycles",
			"Tempo de Simulação",
			"Simulation time",
			"Selecione apenas ",
			"Choose only ",
			" instruções!",
			" instructions!", //[330]
			"Selecione ",
			"Choose ",
			"Resultados da simulação",
			"Simulation results",
			"Resultados rISA",
			"rISA results",
			"Resultados simulação",
			"Simulation results",
			"Instruções descartadas",
			"Discarded intructions", //[340]
			"",
			"",
			"Instruções descartadas",
			"Discarded instructions",
			"Estatísticas da simulação",
			"Simulation statistics",
			"Salvar",
			"Save",
			"Escolha as instruções",
			"Choose the instructions" //[350]
	};
	
	//Lang.iLang==ENGLISH?Lang.msgsGUI[]:Lang.msgsGUI[]
	private static int DefaultLanguage = PORTUGUESE;
}
