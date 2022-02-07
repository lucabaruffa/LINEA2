package linea2;

public class Code128
{
  private static int testNumeric(char[] text, int i, int mini)
  {
    mini--;
    if (i + mini < text.length)
      for (; mini >= 0; mini--)
        if ((text[i + mini] < 48) || (text[i + mini] > 57))
          break;
    return mini;
  }

  /**
   * Barcode 128 encoder...</br>
   * Example: System.out.println(Code128.codeIt("Hello World"));
   *
   * @param textToCode the text you want to code
   * @return the encoded text
   */
  public static String codeIt(String textToCode)
  {
    char[] text = textToCode.toCharArray();
    int checksum = 0; // caractère de vérification du texte codé
    int mini; // nb de caractères numériques suivants
    int char2; // traitement de 2 caractères à la fois
    boolean tableB = true; // booléen pour vérifier si on doit utiliser la table B du code 128

    String code128 = "";

    for (char c : text)
      if ((c < 32) || (c > 126))
        return null;

    for (int i = 0; i < text.length;)
    {
      if (tableB)
      {
        // intéressant de passer en table C pour 4 chiffres au début ou a la fin ou pour 6 chiffres
        mini = ((i == 0) || (i + 3 == text.length - 1) ? 4 : 6);

        // si les mini caractères à partir de index sont numériques, alors mini = 0
        mini = testNumeric(text, i, mini);

        // si mini < 0 on passe en table C
        if (mini < 0)
        {
          code128 += (char) (i == 0 ? 210 : 204); // débuter sur la table C ou commuter sur la table C
          tableB = false;
        }
        else if (i == 0)
          code128 += (char) 209; // débuter sur la table B
      }

      if (!tableB)
      {
        // on est sur la table C, on va essayer de traiter 2 chiffres
        mini = testNumeric(text, i, 2);

        if (mini < 0)
        {
          // ok pour 2 chiffres, les traiter
          char2 = Integer.parseInt("" + text[i] + text[i + 1]);
          char2 += (char2 < 95 ? 32 : 100);
          code128 += (char) char2;
          i += 2;
        }
        else
        {
          // on n'a pas deux chiffres, retourner en table B
          code128 += (char) 205;
          tableB = true;
        }
      }

      if (tableB)
        code128 += text[i++];
    }

    // calcul de la clef de controle
    for (int i = 0; i < code128.length(); i++)
    {
      char2 = code128.charAt(i);
      char2 -= (char2 < 127 ? 32 : 100);
      checksum = ((i == 0 ? char2 : checksum) + i * char2) % 103;
    }

    // calcul du code ascii de la clef de controle
    checksum += (checksum < 95 ? 32 : 100);

    // ajout de la clef et du stop à la fin du texte codé.
    return code128 += ("" + (char) checksum + (char) 211);
  }

}