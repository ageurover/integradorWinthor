/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package integradorbrasil;

/**
 * @author Desconhecido
 * Classe para escrever um valor por extenso
 */
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 * @author Ageu Rover
 */
public class Extenso {


   private ArrayList nro;
   private BigInteger num;

   private String Qualificadores[][] = {
         {"centavo", "centavos"},
         {"", ""},
         {"mil", "mil"},
         {"milhao", "milhoes"},
         {"bilhao", "bilhoes"},
         {"trilhao", "trilhoes"},
         {"quatrilhao", "quatrilhoes"},
         {"quintilhao", "quintilhoes"},
         {"sextilhao", "sextilhoes"},
         {"septilhao", "septilhoes"}
         };
   private String Numeros[][] = {
         {"zero", "um", "dois", "tres", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez",
         "onze", "doze", "treze", "quatorze", "quinze", "desesseis", "desessete", "dezoito", "desenove"},
         {"vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"},
         {"cem", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos",
         "setecentos", "oitocentos", "novecentos"}
         };


   /**
    *  Construtor
    */
   public Extenso() {
      nro = new ArrayList();
   }


   /**
    *  Construtor
    *
    *@param  dec  valor para colocar por extenso
    */
   public Extenso(BigDecimal dec) {
      this();
      setNumber(dec);
   }


   /**
    *  Construtor para colocar o objeto por extenso
    *
    *@param  dec  valor para colocar por extenso
    */
   public Extenso(double dec) {
      this();
      setNumber(dec);
   }


   /**
    *  Setando o atributo do n�mero para coloc�-lo por extenso
    *
    *@param  dec  Novo valor para o N�mero
    */
   public void setNumber(BigDecimal dec) {
      // Converte para inteiro arredondando os centavos
      num = dec
         .setScale(2, BigDecimal.ROUND_HALF_UP)
         .multiply(BigDecimal.valueOf(100))
         .toBigInteger();

      // Adiciona valores
      nro.clear();
      if (num.equals(BigInteger.ZERO)) {
         // Centavos
         nro.add(new Integer(0));
         // Valor
         nro.add(new Integer(0));
      }
      else {
         // Adiciona centavos
         addRemainder(100);

         // Adiciona grupos de 1000
         while (!num.equals(BigInteger.ZERO)) {
            addRemainder(1000);
         }
      }
   }

   public void setNumber(double dec) {
      setNumber(new BigDecimal(dec));
   }

   /**
    *  Descri��o do M�todo
    */
   public void show() {
      Iterator valores = nro.iterator();

      while (valores.hasNext()) {
         System.out.println(((Integer) valores.next()).intValue());
      }
      System.out.println(toString());
   }


   /**
    *  Descri��o do M�todo
    *
    *@return    Descri��o do Valor Retornado
    */
    @Override
   public String toString() {
      StringBuffer buf = new StringBuffer();

      int numero = ((Integer) nro.get(0)).intValue();
      int ct;

      for (ct = nro.size() - 1; ct > 0; ct--) {
         // Se ja existe texto e o atual n�o � zero
         if (buf.length() > 0 && ! ehGrupoZero(ct)) {
            buf.append(" e ");
         }
         buf.append(numToString(((Integer) nro.get(ct)).intValue(), ct));
      }
      if (buf.length() > 0) {
         if (ehUnicoGrupo())
            buf.append(" de ");
         while (buf.toString().endsWith(" "))
            buf.setLength(buf.length()-1);
         if (ehPrimeiroGrupoUm())
            buf.insert(0, "h");
         if (nro.size() == 2 && ((Integer)nro.get(1)).intValue() == 1) {
            buf.append(" real");
         } else {
            buf.append(" reais");
         }
         if (((Integer) nro.get(0)).intValue() != 0) {
            buf.append(" e ");
         }
      }
      if (((Integer) nro.get(0)).intValue() != 0) {
         buf.append(numToString(((Integer) nro.get(0)).intValue(), 0));
      }
      return buf.toString();
   }

   private boolean ehPrimeiroGrupoUm() {
      if (((Integer)nro.get(nro.size()-1)).intValue() == 1)
         return true;
      return false;
   }

   /**
    *  Adds a feature to the Remainder attribute of the Extenso object
    *
    *@param  divisor  The feature to be added to the Remainder attribute
    */
   private void addRemainder(int divisor) {
      // Encontra newNum[0] = num modulo divisor, newNum[1] = num dividido divisor
      BigInteger[] newNum = num.divideAndRemainder(BigInteger.valueOf(divisor));

      // Adiciona modulo
      nro.add(new Integer(newNum[1].intValue()));

      // Altera numero
      num = newNum[0];
   }


   /**
    *  Descri��o do M�todo
    *
    *@param  ps  Descri��o do Par�metro
    *@return     Descri��o do Valor Retornado
    */
   private boolean temMaisGrupos(int ps) {
      for (; ps > 0; ps--) {
         if (((Integer) nro.get(ps)).intValue() != 0) {
            return true;
         }
      }

      return false;
   }


   /**
    *  Descri��o do M�todo
    *
    *@param  ps  Descri��o do Par�metro
    *@return     Descri��o do Valor Retornado
    */
   private boolean ehUltimoGrupo(int ps) {
      return (ps > 0) && ((Integer)nro.get(ps)).intValue() != 0 && !temMaisGrupos(ps - 1);
   }


   /**
    *  Descri��o do M�todo
    *
    *@return     Descri��o do Valor Retornado
    */
   private boolean ehUnicoGrupo() {
      if (nro.size() <= 3)
         return false;
      if (!ehGrupoZero(1) && !ehGrupoZero(2))
         return false;
      boolean hasOne = false;
      for(int i=3; i < nro.size(); i++) {
         if (((Integer)nro.get(i)).intValue() != 0) {
            if (hasOne)
               return false;
            hasOne = true;
         }
      }
      return true;
   }

   boolean ehGrupoZero(int ps) {
      if (ps <= 0 || ps >= nro.size())
         return true;
      return ((Integer)nro.get(ps)).intValue() == 0;
   }

   /**
    *  Descri��o do M�todo
    *
    *@param  numero  Descri��o do Par�metro
    *@param  escala  Descri��o do Par�metro
    *@return         Descri��o do Valor Retornado
    */
   private String numToString(int numero, int escala) {
      int unidade = (numero % 10);
      int dezena = (numero % 100); //* nao pode dividir por 10 pois verifica de 0..19
      int centena = (numero / 100);
      StringBuffer buf = new StringBuffer();

      if (numero != 0) {
         if (centena != 0) {
            if (dezena == 0 && centena == 1) {
               buf.append(Numeros[2][0]);
            }
            else {
               buf.append(Numeros[2][centena]);
            }
         }

         if ((buf.length() > 0) && (dezena != 0)) {
            buf.append(" e ");
         }
         if (dezena > 19) {
            dezena /= 10;
            buf.append(Numeros[1][dezena - 2]);
            if (unidade != 0) {
               buf.append(" e ");
               buf.append(Numeros[0][unidade]);
            }
         }
         else if (centena == 0 || dezena != 0) {
            buf.append(Numeros[0][dezena]);
         }

         buf.append(" ");
         if (numero == 1) {
            buf.append(Qualificadores[escala][0]);
         }
         else {
            buf.append(Qualificadores[escala][1]);
         }
      }

      return buf.toString();
   }


   /**
    *  Para teste
    *
    *@param  args  numero a ser convertido
    */
   public static void main(String[] args) {
      if (args.length == 0) {
         System.out.println("Sintax : ...Extenso <numero>");
         return;   
      }
//      Extenso teste = new Extenso(new BigDecimal(args[0]));
//      System.out.println("Numero  : " + (new DecimalFormat().format(Double.valueOf(args[0]))));
//      System.out.println("Extenso : " + teste.toString());
   }
}
