
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class mian {
    public static void main(String args[]) {
        iniciarPartido(datosPartido());        
    }

    static Map<String, String> datosPartido(){
        Map<String, String> datosPartido = new HashMap<>();

        //Ingresar nombre del torneo
        System.out.println("Nombre del torneo: ");
        Scanner nombreTorneo = new Scanner(System.in);
        datosPartido.put("torneo", nombreTorneo.nextLine());
        //Ingresar nombre del jugador 1
        System.out.println("Jugador 1: ");
        Scanner jugador1 = new Scanner(System.in);  
        datosPartido.put("jugador1", jugador1.nextLine());
        //Ingresar nombre del jugador 2
        System.out.println("Jugador 2: ");
        Scanner jugador2 = new Scanner(System.in);  
        datosPartido.put("jugador2", jugador2.nextLine());
        //Ingresar cantidad de sets
        boolean respuestaSets = true;
        while(respuestaSets){
            System.out.println("Ingrese la cantidad de sets (3 o 5): ");
            Scanner sets = new Scanner(System.in);  
            String respuesta = sets.nextLine();
            if(respuesta.equals("3")||respuesta.equals("5")){
                datosPartido.put("sets", respuesta);
                respuestaSets = false;
            }
        }
        //Ingresar probabilidades del jugador 1
        boolean respuestaProbabilidad = true;
        while(respuestaProbabilidad){
            System.out.println("Ingrese la probabilidad de victoria del jugador 1 (1 al 100): ");
            
            try{
                Scanner probabilidad = new Scanner(System.in); 
                int respuesta = probabilidad.nextInt();
                if(respuesta>0 && respuesta<101){
                    datosPartido.put("probabilidadJugador1", String.valueOf(respuesta));
                    datosPartido.put("probabilidadJugador2", String.valueOf(100 - Integer.valueOf(datosPartido.get("probabilidadJugador1"))));
                    respuestaProbabilidad=false;
                }
            } catch (Exception e){
                System.out.println("No se puede ingresar string");
            }
            
            
        }
        
        return datosPartido;
    }
    static void iniciarPartido(Map<String, String> datosPartido){
        System.out.println(
            "\n\nDATOS DE LA PARTIDA \nJugador 1: " 
            + datosPartido.get("jugador1")
            + "\nJugador 2: "
            + datosPartido.get("jugador2")
            + "\nCantidad de sets: "
            + datosPartido.get("sets")
            + "\nProbabilidad de victoria para " + datosPartido.get("jugador1")
            + ": " + datosPartido.get("probabilidadJugador1")
            + "\nProbabilidad de victoria para " + datosPartido.get("jugador2")
            + ": " + datosPartido.get("probabilidadJugador2")
            + "\n\nEscriba LISTO para comenzar con la partida: "
        );
        Scanner comienzoDeJuego = new Scanner(System.in);
        if(comienzoDeJuego.nextLine().toUpperCase().equals("LISTO")){
            System.out.println("\nEl juego empezará pronto... \n");
            partido(datosPartido);
        } else {
            iniciarPartido(datosPartido);
        }
    }     
    static void partido(Map<String, String> datosPartido){
        Map<String, Object> datosSets = new HashMap<>();
        datosSets.put("setActual", 1);
        datosSets.put("cantidadSetsJugador1", 0);
        datosSets.put("cantidadSetsJugador2", 0);
        Map<String, Object> datosGames = new HashMap<>();
        datosGames.put("gameActual", 1);
        datosGames.put("puntajeDeGameActualJugador1", "0");
        datosGames.put("puntajeDeGameActualJugador2", "0");
        datosGames.put("gamesGanadosJugador1", 0);
        datosGames.put("gamesGanadosJugador2", 0);
        datosGames.put("puntajesGamesJug1", "");
        datosGames.put("puntajesGamesJug2", "");
        datosGames.put("turnoDeSaque", 1);

        System.out.println("¡Comenzó el partido!");
        System.out.println("Se está jugando el GAME "+datosGames.get("gameActual")+" - SET "+datosSets.get("setActual"));
        while(true){
            try {
                Thread.sleep(200);
                
                List<Map<String, Object>> datos = games(datosPartido, datosGames, datosSets);
                datosGames = datos.get(0);
                datosSets = datos.get(1);

                if((int)datosGames.get("turnoDeSaque")==1){
                    System.out.println("\n(*)"+datosPartido.get("jugador1") + " " + datosGames.get("puntajeDeGameActualJugador1") + " " + datosGames.get("puntajeDeGameActualJugador2") + " "+datosPartido.get("jugador2"));
                } else {
                    System.out.println("\n"+datosPartido.get("jugador1") + " " + datosGames.get("puntajeDeGameActualJugador1") + " " + datosGames.get("puntajeDeGameActualJugador2") + " (*)"+datosPartido.get("jugador2"));
                }
                
                String setsTotales = datosPartido.get("sets").toString();
                if((int)datosSets.get("cantidadSetsJugador1") == 2 && setsTotales.equals("3") ||
                    (int)datosSets.get("cantidadSetsJugador1") == 3 && setsTotales.equals("5")){
                    finalizarPartido(datosSets, datosPartido, datosGames);
                    return;
                }
                if((int)datosSets.get("cantidadSetsJugador2") == 2 && setsTotales.equals("3") ||
                    (int)datosSets.get("cantidadSetsJugador2") == 3 && setsTotales.equals("5")){
                    finalizarPartido(datosSets, datosPartido, datosGames);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    
    }
    static List<Map<String, Object>> games(Map<String, String> datosPartido, Map<String, Object> datosGames, Map<String, Object> datosSets){
        int ganadorDelPunto = probabilidad(datosPartido);
        if(ganadorDelPunto == 1){
            if((int)datosGames.get("gamesGanadosJugador1")==6 && (int)datosGames.get("gamesGanadosJugador2")==6){
                datosGames.put("puntajeDeGameActualJugador1", String.valueOf(Integer.valueOf((String)datosGames.get("puntajeDeGameActualJugador1"))+1));
                int puntajeActualJug1 = Integer.valueOf((String)datosGames.get("puntajeDeGameActualJugador1"));
                int puntajeActualJug2 = Integer.valueOf((String)datosGames.get("puntajeDeGameActualJugador2"));

                if(puntajeActualJug1==7 && puntajeActualJug2<6
                    || puntajeActualJug1>=7 && puntajeActualJug2>=7 && (puntajeActualJug1-puntajeActualJug2)>1){
                        gameGanado(ganadorDelPunto, datosGames, datosSets, datosPartido);
                }
                if((int)datosGames.get("turnoDeSaque")==1){
                    datosGames.put("turnoDeSaque", 2);
                } else {
                    datosGames.put("turnoDeSaque", 1);                
                }
            } else {
                switch(datosGames.get("puntajeDeGameActualJugador1").toString()){
                    case "0":
                        datosGames.put("puntajeDeGameActualJugador1", "15");
                        break;
                    case "15":
                        datosGames.put("puntajeDeGameActualJugador1", "30");
                        break;
                    case "30":
                        datosGames.put("puntajeDeGameActualJugador1", "40");
                        break;   
                    case "40":
                        switch(datosGames.get("puntajeDeGameActualJugador2").toString()){
                            case "40":
                                datosGames.put("puntajeDeGameActualJugador1", "AC");
                                break;
                            case "AC":
                                datosGames.put("puntajeDeGameActualJugador2", "40");
                                break;
                            default:
                                List<Map<String, Object>> datos = gameGanado(ganadorDelPunto, datosGames, datosSets, datosPartido);
                                datosGames = datos.get(0);
                                datosSets = datos.get(1);
                                break;
                            }
                            break;
                    case "AC":
                        List<Map<String, Object>> datos = gameGanado(ganadorDelPunto, datosGames, datosSets, datosPartido);
                        datosGames = datos.get(0);
                        datosSets = datos.get(1);
                        break;
                }
            }      
        }
        if(ganadorDelPunto == 2){
            if((int)datosGames.get("gamesGanadosJugador1")==6 && (int)datosGames.get("gamesGanadosJugador2")==6){
                datosGames.put("puntajeDeGameActualJugador2", String.valueOf(Integer.valueOf((String)datosGames.get("puntajeDeGameActualJugador2"))+1));
                int puntajeActualJug1 = Integer.valueOf((String)datosGames.get("puntajeDeGameActualJugador1"));
                int puntajeActualJug2 = Integer.valueOf((String)datosGames.get("puntajeDeGameActualJugador2"));

                if(puntajeActualJug2==7 && puntajeActualJug1<6
                    || puntajeActualJug1>=7 && puntajeActualJug2>=7 && (puntajeActualJug2-puntajeActualJug1)>1){
                        gameGanado(ganadorDelPunto, datosGames, datosSets, datosPartido);
                }
                if((int)datosGames.get("turnoDeSaque")==1){
                    datosGames.put("turnoDeSaque", 2);
                } else {
                    datosGames.put("turnoDeSaque", 1);                
                }
            } else {
                switch(datosGames.get("puntajeDeGameActualJugador2").toString()){
                    case "0":
                        datosGames.put("puntajeDeGameActualJugador2", "15");
                        break;
                    case "15":
                        datosGames.put("puntajeDeGameActualJugador2", "30");
                        break;
                    case "30":
                        datosGames.put("puntajeDeGameActualJugador2", "40");
                        break;   
                    case "40":
                        switch(datosGames.get("puntajeDeGameActualJugador1").toString()){
                            case "40":
                                datosGames.put("puntajeDeGameActualJugador2", "AC");
                                break;
                            case "AC":
                                datosGames.put("puntajeDeGameActualJugador1", "40");
                                break;
                            default:
                                List<Map<String, Object>> datos = gameGanado(ganadorDelPunto, datosGames, datosSets, datosPartido);
                                datosGames = datos.get(0);
                                datosSets = datos.get(1);
                                break;
                        }
                        break;
                    case "AC":
                        List<Map<String, Object>> datos = gameGanado(ganadorDelPunto, datosGames, datosSets, datosPartido);
                        datosGames = datos.get(0);
                        datosSets = datos.get(1);
                        break;
                }
            }
        }
        return List.of(datosGames, datosSets);
    }  
    static List<Map<String, Object>> gameGanado(int ganadorDelPunto, Map<String, Object> datosGames, Map<String, Object> datosSets,Map<String, String> datosPartido){
        if(ganadorDelPunto==1){
            if((int)datosGames.get("gamesGanadosJugador1")==6){
                if((int)datosGames.get("turnoDeSaque")==1){
                    System.out.println("\n(*)"+datosPartido.get("jugador1") + " " + datosGames.get("puntajeDeGameActualJugador1") + " " + datosGames.get("puntajeDeGameActualJugador2") + " "+datosPartido.get("jugador2"));
                } else {
                    System.out.println("\n"+datosPartido.get("jugador1") + " " + datosGames.get("puntajeDeGameActualJugador1") + " " + datosGames.get("puntajeDeGameActualJugador2") + " (*)"+datosPartido.get("jugador2"));
                }
            }
            datosGames.put("gamesGanadosJugador1", (int)datosGames.get("gamesGanadosJugador1")+1);
            System.out.println("\nGame "+ (int)datosGames.get("gameActual") + " ganado por "+datosPartido.get("jugador1"));
            System.out.println("Resultado parcial del set: "
            +datosPartido.get("jugador1")+" ("+(int)datosGames.get("gamesGanadosJugador1")
            +") ("+(int)datosGames.get("gamesGanadosJugador2")+") "+datosPartido.get("jugador2"));
            datosGames.put("gameActual", (int)datosGames.get("gameActual")+1);

            int gamesGanadosJugador1 = (int)datosGames.get("gamesGanadosJugador1");
            int gamesGanadosJugador2 = (int)datosGames.get("gamesGanadosJugador2");

            if((int)datosGames.get("turnoDeSaque")==1){
                datosGames.put("turnoDeSaque", 2);
            } else {
                datosGames.put("turnoDeSaque", 1);                
            }
            
            if(gamesGanadosJugador1==6&&gamesGanadosJugador2<5 || gamesGanadosJugador1==7&&gamesGanadosJugador2==5 || gamesGanadosJugador1==7&&gamesGanadosJugador2==6){
                System.out.println("\n"+datosPartido.get("jugador1").toUpperCase()+" ganó el set "+(int)datosSets.get("setActual"));

                datosGames.put("gameActual", 1);
                
                if(gamesGanadosJugador1==7&&gamesGanadosJugador2==6){
                    datosGames.put("puntajesGamesJug1", (String)datosGames.get("puntajesGamesJug1")+datosGames.get("gamesGanadosJugador1")+"("+datosGames.get("puntajeDeGameActualJugador1")+") ");
                    datosGames.put("puntajesGamesJug2", (String)datosGames.get("puntajesGamesJug2")+datosGames.get("gamesGanadosJugador2")+"("+datosGames.get("puntajeDeGameActualJugador2")+") ");
                }else {
                    datosGames.put("puntajesGamesJug1", (String)datosGames.get("puntajesGamesJug1")+datosGames.get("gamesGanadosJugador1")+" ");
                    datosGames.put("puntajesGamesJug2", (String)datosGames.get("puntajesGamesJug2")+datosGames.get("gamesGanadosJugador2")+" ");
                }
                datosGames.put("gamesGanadosJugador1", 0);
                datosGames.put("gamesGanadosJugador2", 0);
                datosSets.put("setActual", (int)datosSets.get("setActual")+1);
                datosSets.put("cantidadSetsJugador1", (int)datosSets.get("cantidadSetsJugador1")+1);
                System.out.println("Resultado parcial del partido: "
                    +datosPartido.get("jugador1")+" ("+(int)datosSets.get("cantidadSetsJugador1")
                    +") ("+(int)datosSets.get("cantidadSetsJugador2")+") "+datosPartido.get("jugador2"));
                System.out.println("\nSe está jugando el GAME "+datosGames.get("gameActual")+" - SET "+datosSets.get("setActual"));
            } else {
                System.out.println("\nSe está jugando el GAME "+datosGames.get("gameActual")+" - SET "+datosSets.get("setActual"));
            }
            datosGames.put("puntajeDeGameActualJugador1", "0");
            datosGames.put("puntajeDeGameActualJugador2", "0");
        } else {
            if((int)datosGames.get("gamesGanadosJugador2")==6){
                if((int)datosGames.get("turnoDeSaque")==1){
                    System.out.println("\n(*)"+datosPartido.get("jugador1") + " " + datosGames.get("puntajeDeGameActualJugador1") + " " + datosGames.get("puntajeDeGameActualJugador2") + " "+datosPartido.get("jugador2"));
                } else {
                    System.out.println("\n"+datosPartido.get("jugador1") + " " + datosGames.get("puntajeDeGameActualJugador1") + " " + datosGames.get("puntajeDeGameActualJugador2") + " (*)"+datosPartido.get("jugador2"));
                }
            }
            datosGames.put("gamesGanadosJugador2", (int)datosGames.get("gamesGanadosJugador2")+1);
            System.out.println("\nGame "+ (int)datosGames.get("gameActual") + " ganado por "+datosPartido.get("jugador2"));
            System.out.println("Resultado parcial del set: "
            +datosPartido.get("jugador1")+" ("+(int)datosGames.get("gamesGanadosJugador1")
            +") ("+(int)datosGames.get("gamesGanadosJugador2")+") "+datosPartido.get("jugador2"));
            datosGames.put("gameActual", (int)datosGames.get("gameActual")+1);

            int gamesGanadosJugador1 = (int)datosGames.get("gamesGanadosJugador1");
            int gamesGanadosJugador2 = (int)datosGames.get("gamesGanadosJugador2");

            if((int)datosGames.get("turnoDeSaque")==1){
                datosGames.put("turnoDeSaque", 2);
            } else {
                datosGames.put("turnoDeSaque", 1);                
            }
            if(gamesGanadosJugador2==6&&gamesGanadosJugador1<5 || gamesGanadosJugador2==7&&gamesGanadosJugador1==5 || gamesGanadosJugador2==7&&gamesGanadosJugador1==6){
                System.out.println("\n"+datosPartido.get("jugador2").toUpperCase()+" ganó el set "+(int)datosSets.get("setActual"));

                datosGames.put("gameActual", 1);
                if(gamesGanadosJugador2==7&&gamesGanadosJugador1==6){
                    datosGames.put("puntajesGamesJug1", (String)datosGames.get("puntajesGamesJug1")+datosGames.get("gamesGanadosJugador1")+"("+datosGames.get("puntajeDeGameActualJugador1")+") ");
                    datosGames.put("puntajesGamesJug2", (String)datosGames.get("puntajesGamesJug2")+datosGames.get("gamesGanadosJugador2")+"("+datosGames.get("puntajeDeGameActualJugador2")+") ");
                }else {
                    datosGames.put("puntajesGamesJug1", (String)datosGames.get("puntajesGamesJug1")+datosGames.get("gamesGanadosJugador1")+" ");
                    datosGames.put("puntajesGamesJug2", (String)datosGames.get("puntajesGamesJug2")+datosGames.get("gamesGanadosJugador2")+" ");
                }
                datosGames.put("gamesGanadosJugador1", 0);
                datosGames.put("gamesGanadosJugador2", 0);

                int cantidadSetsJugador2 = (int)datosSets.get("cantidadSetsJugador2");
                datosSets.put("setActual", (int)datosSets.get("setActual")+1);
                datosSets.put("cantidadSetsJugador2", cantidadSetsJugador2+1);
                System.out.println("Resultado parcial del partido: "
                    +datosPartido.get("jugador1")+" ("+(int)datosSets.get("cantidadSetsJugador1")
                    +") ("+(int)datosSets.get("cantidadSetsJugador2")+") "+datosPartido.get("jugador2"));
                System.out.println("Se está jugando el GAME "+datosGames.get("gameActual")+" - SET "+datosSets.get("setActual"));
            }  else {
                System.out.println("\nSe está jugando el GAME "+datosGames.get("gameActual")+" - SET "+datosSets.get("setActual"));
            }
            datosGames.put("puntajeDeGameActualJugador1", "0");
            datosGames.put("puntajeDeGameActualJugador2", "0");
        }
        return List.of(datosGames, datosSets);
    }
    static void finalizarPartido(Map<String, Object> datosSets, Map<String, String> datosPartido, Map<String, Object> datosGames){
        System.out.println("\n¡FINAL DEL PARTIDO!\n"
            +datosPartido.get("jugador1")+" "+datosGames.get("puntajesGamesJug1")
            +"\n"+datosPartido.get("jugador2")+" "+datosGames.get("puntajesGamesJug2"));
        if((int)datosSets.get("cantidadSetsJugador1")>(int)datosSets.get("cantidadSetsJugador2")){
            System.out.println("El ganador del torneo "+ datosPartido.get("torneo") + " es "+datosPartido.get("jugador1"));
        } else {
            System.out.println("El ganador del torneo "+ datosPartido.get("torneo") + " es "+datosPartido.get("jugador2"));
        }
        revancha(datosPartido);
    }
    static void revancha(Map<String, String> datosPartido){
        System.out.println("\n¿Desea jugar la revancha?[S/N]");
        Scanner opcionRevancha = new Scanner(System.in);
        String resultado = opcionRevancha.nextLine();
        switch(resultado.toUpperCase()){
            case "S":
                iniciarPartido(datosPartido);
                break;
            case "N":
                return;
            default:
                System.out.println("Ingrese una opcion correcta");
                revancha(datosPartido);
                break;
        }

    }
    static int probabilidad(Map<String, String> datosPartido){
        int numberRandom = (int)Math.floor(Math.random()*100);
        if(numberRandom <= Integer.valueOf(datosPartido.get("probabilidadJugador1"))){
            return 1;
        }
        return 2;
    } 
}

