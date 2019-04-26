public class Scanner {
   private String input;
   private int pos;

   public Scanner(String input) {
      this.input = input;
      this.pos = 0;
   }

   public String getInput() {
      return input;
   }

   public void setInput(String input) {
      this.input = input;
   }

   public int getPos() {
      return pos;
   }

   public void setPos(int pos) {
      this.pos = pos;
   }

   public Token nextToken() {
      Token token;
      int state;
      char chr;

      if (pos == input.length())
         return new Token(Names.EOF);

      state = 0;
      chr = input.charAt(pos);
      while(true) {
         if (getPos() >= getInput().length())
            return new Token(Names.EOF);

         switch (state) {
            case 0:
               if (getPos() == getInput().length())
                  return new Token(Names.EOF);

               else if (Character.isLetterOrDigit(chr) || isUnderscore(chr))
                  state = 1;
               else if (chr == ' ')
                  state = 2;
               else
                  lexicalError();
               pos ++;
               break;
            case 1:  // ID
               String lexema = "";
               lexema += String.valueOf(chr);

               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());

                  if (Character.isLetterOrDigit(chr) || isUnderscore(chr)) {
                     lexema += String.valueOf(chr);
                     incrementPos();
                  } else
                     break;
               }
               state = 0;
               return new Token(Names.ID, lexema);
            case 2:  // Espaço em branco
               while (getPos() < getInput().length()) {
                  chr = input.charAt(getPos());
                  if (chr == ' ')
                     incrementPos();
                  else
                     break;
               }
               state = 0;
               break;
            default:
               lexicalError();
         }
      }
   }

   private void incrementPos() {
      setPos(getPos() + 1);
   }

   private void lexicalError() {
      System.out.println("Token mal formado");
      System.exit(1);
   }

   private boolean isUnderscore(char chr) {
      return (chr == 95);
   }
}
