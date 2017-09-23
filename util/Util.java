package mobi.stos.projetoestacio.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.swing.text.MaskFormatter;
import mobi.stos.projetoestacio.exception.FileTypeNotAllowedException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Util {

    private static String[] allowTypes;
    public static final Locale LOCAL = new Locale("pt", "BR");

    public Util() {
        String[] allowedTypes = new String[3];
        allowedTypes[0] = "image/jpeg";
        allowedTypes[1] = "image/gif";
        allowedTypes[2] = "image/png";
        Util.allowTypes = allowedTypes;
    }

    /**
     * Método estático (static) que converte uma string qualquer em um arquivo
     * hash de criptografia MD5, gerando 32 algarismos.<br /> Impossível reaver
     * o arquivo após sua criptografia.
     *
     * @param senha String de qualquer tamanho;
     * @return String Retorna um arquivo criptografado no formato MD5.
     * @exception NoSuchAlgorithmException Esta exceção é lançada quando um
     * determinado algoritmo criptográfico é solicitadas, mas não está
     * disponível no ambiente.
     */
    public static String md5(String senha) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        String hash2 = hash.toString(16);
        if (hash2.length() < 32) {
            int max = 32 - hash2.length();
            for (int i = 0; i < max; i++) {
                hash2 = "0" + hash2;
            }
        }
        return hash2;
    }

    /**
     * Função converte long para Date. Formato de conversão dd/MM/yyyy HH:mm:ss
     *
     * @param d long
     * @return String
     */
    public static String longToDate(long d) {
        Date dt = new Date(d);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("America/Recife"));
        return df.format(dt);
    }

    /**
     * Função remove todos os caracteres HTML de um texto, deixando apenas os
     * textos livres. Atualizaçao: Também é feita a remoção dos comando
     * <strong>&amp;nbsp;</strong>
     *
     * @param text String
     * @return String
     */
    public static String stripTags(String text) {
        return text.replaceAll("\\<.*?\\>", "").replaceAll("&nbsp;", "").trim();
    }

    /**
     * Função remove todos os caracteres HTML de um texto, deixando apenas os
     * textos livres. Atualizaçao: Também é feita a remoção dos comando
     * <strong>&amp;nbsp;</strong> Escolhendo a opção de manter como true você
     * está de acordo em manter as quebras de linhas.
     *
     * @param text String
     * @param manter boolean
     * @return String
     */
    public static String stripTags(String text, boolean manter) {

        text = text.replaceAll("<br>", "<br />");
        text = text.replaceAll("</p>", "<br />");
        text = text.replaceAll("</h1>", "<br />");
        text = text.replaceAll("</h2>", "<br />");
        text = text.replaceAll("</h3>", "<br />");
        text = text.replaceAll("</h4>", "<br />");
        text = text.replaceAll("</h5>", "<br />");
        text = text.replaceAll("</h6>", "<br />");

        String string = "";
        String[] a = text.split("<br />");
        for (String s : a) {
            string += s.replaceAll("\\<.*?\\>", "").replaceAll("&nbsp;", "").trim() + "\r\n";
        }
        return string;
    }

    /**
     * Função estatica para adicionar zero a esquerda e manté-los no respectivo
     * numeral. <br /> O valor de zeros para adicionar não deve ser superior a
     * String completa, por exemplo. <br /><br />
     *
     * valor = 123<br /> zeros = 2<br /><br />
     *
     * Retornará exceção a quantidade de zeros deve ser igual ou maior ao
     * numeral informado.<br /><br />
     *
     * Forma correta:<br /><br />
     *
     * valor = 123<br /> zeros = 4<br /><br /> retorno 0123 (quatro algarimos)
     *
     *
     * @param valor Object Valor que deseja adicionar zeros
     * @param zeros int quantidade de zeros para adicionar
     * @return String
     */
    public static String zeroFill(Object valor, int zeros) {
        String sValor = String.valueOf(valor);

        if (sValor.length() > zeros) {
            return sValor;
        }

        int restantes = zeros - sValor.length();
        String zadd = "";
        for (int i = 0; i < restantes; i++) {
            zadd += "0";
        }

        return zadd.concat(sValor);
    }

    /**
     * Função estatica para obter o primeiro dia do mês
     *
     * @param ano Integer
     * @param mes Integer
     * @return String (DD/MM/YYYY)
     */
    public static String firstDayOfMonth(Integer ano, Integer mes) throws Exception {
        Calendar cal = new GregorianCalendar(ano, mes - 1, 1);
        return Util.zeroFill(cal.getActualMinimum(Calendar.DAY_OF_MONTH), 2) + "/" + Util.zeroFill(mes, 2) + "/" + ano.toString();
    }

    /**
     * Função estatica para obter o último dia do mês
     *
     * @param ano Integer
     * @param mes Integer
     * @return String (DD/MM/YYYY)
     */
    public static String lastDayOfMonth(Integer ano, Integer mes) throws Exception {
        Calendar cal = new GregorianCalendar(ano, mes - 1, 1);
        return Util.zeroFill(cal.getActualMaximum(Calendar.DAY_OF_MONTH), 2) + "/" + Util.zeroFill(mes, 2) + "/" + ano.toString();
    }

    /**
     * função de apoio para carregamento dos tipos permitidos.
     *
     * @param allowTypes String[]
     */
    public static void setAllowTypes(String[] allowTypes) {
        Util.allowTypes = allowTypes;
    }

    /**
     * Função verifica as extensões permitidas.<br /> Deve ser previamente
     * carregada as extensões que deseja testar em sua aplicação.<br /> Caso não
     * carregamento ou falha no carregamento será adotado os seguintes valores:
     * "image/jpeg"; "image/gif"; "image/png";
     *
     * @param contentType String
     * @throws FileTypeNotAllowedException
     */
    public static void allowedTypes(String contentType) throws FileTypeNotAllowedException {
        boolean r = false;
        String[] strangetypes = new String[3];
        strangetypes[0] = "application/force-download";
        strangetypes[1] = "application/x-real";
        strangetypes[2] = "document/unknown";

        // aplicação diferente
        for (String s : strangetypes) {
            if (contentType.equals(s)) {
                r = true;
                break;
            }
        }

        if (!r) {
            for (String s : Util.allowTypes) {
                if (contentType.equals(s)) {
                    r = true;
                    break;
                }
            }
        }

        if (!r) {

            String ex = "";
            for (String s : Util.allowTypes) {
                ex += s + ", ";
            }
            if (!ex.equals("")) {
                ex = ex.substring(0, ex.length() - 2);
            }

            throw new FileTypeNotAllowedException("Permitido apenas envio de arquivos do tipo: " + ex);
        }
    }

    /**
     * Função obtem bytes de um arquivo.<br /> CAso o arquivo seja maior que um
     * integer será disparado uma exceção.
     *
     * @param file File
     * @return byte[]
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        long length = file.length();

        if (length > Integer.MAX_VALUE) {
            throw new IOException("Arquivo muito grande!");
        }

        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;

        try {
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            is.close();
        }

        if (offset < bytes.length) {
            throw new IOException("Não foi possível completar a leitura do arquivo " + file.getName());
        }
        return bytes;
    }

    public static byte[] hexToBytes(char[] hex) {
        int length = hex.length / 2;
        byte[] raw = new byte[length];
        for (int i = 0; i < length; i++) {
            int high = Character.digit(hex[i * 2], 16);
            int low = Character.digit(hex[i * 2 + 1], 16);
            int value = (high << 4) | low;
            if (value > 127) {
                value -= 256;
            }
            raw[i] = (byte) value;
        }
        return raw;
    }

    /**
     * Função para encurtar URL. A URL deve começar com http:// ou ftp://
     *
     * @param url String URL completa que deseja encurtar
     * @return String URL Curta
     */
    public static String encurtarUrl(String url) throws Exception {
        if ((url == null) || (url.equals(""))) {
            throw new Exception("Favor informar uma URL válida!");
        }

        url = url.trim();
        //url = url.replace("http://", "");

        URLEncoder.encode(url, "UTF-8");
        URL r = new URL("http://migre.me/api.txt?url=" + url);

        InputStream in = r.openStream();
        StringBuilder sb = new StringBuilder();

        byte[] buffer = new byte[256];

        while (true) {
            int byteRead = in.read(buffer);
            if (byteRead == -1) {
                break;
            }
            for (int i = 0; i < byteRead; i++) {
                sb.append((char) buffer[i]);
            }
        }
        return sb.toString();
    }

    /**
     * Função estatica para mudança de número de String para int
     *
     * @param n String valor referente ao número que deseja passar
     * @return int numero String convertido em inteiro
     */
    public static int parseInt(String n) {
        if ((n == null) || (n.trim().equalsIgnoreCase(""))) {
            return 0;
        } else {
            return Integer.parseInt(n);
        }
    }

    /**
     * Função estatica para mudança de número de String para float
     *
     * @param n String valor referente ao número que deseja passar
     * @return float numero String convertido em float
     */
    public static float parseFloat(String n) {
        if ((n == null) || (n.trim().equalsIgnoreCase(""))) {
            return 0;
        } else {
            return Float.parseFloat(n);
        }
    }

    /**
     * Função estatica para mudança de número de String para float
     *
     * @param n String valor referente ao número que deseja passar
     * @return float numero String convertido em float
     */
    public static BigDecimal parseBigDecimal(String n) {
        if ((n == null) || (n.trim().equalsIgnoreCase(""))) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(Double.valueOf(n));
        }
    }

    /**
     * Função serve para criar novo nome para o arquivo com base a informações
     * de data e hora. Esse método é importante para evitar que existam arquivos
     * duplicados no servidor pois a função gera um nome com até o milesimo de
     * segundo do arquivo.
     *
     * @param extensao String extensão do nome do arquivo
     * @return String
     */
    public static String newFileName(String extensao) {
        Calendar calendar = new GregorianCalendar();
        int dat = calendar.get(Calendar.DAY_OF_MONTH);
        int mon = calendar.get(Calendar.MONTH);
        int yea = calendar.get(Calendar.YEAR);
        int hrs = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        int mil = calendar.get(Calendar.MILLISECOND);

        String photoName = dat + "" + mon + "" + yea + "" + hrs + "" + min + ""
                + sec + "" + mil + extensao;
        return photoName;
    }

    /**
     * Função calcula a idade.
     *
     * @param dataNasc Date Data de Nascimento
     * @return int idada
     */
    public static int calculaIdade(Date dataNasc) {
        Calendar dateOfBirth = new GregorianCalendar();
        dateOfBirth.setTime(dataNasc);

        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        dateOfBirth.add(Calendar.YEAR, age);
        if (today.before(dateOfBirth)) {
            age--;
        }
        return age;
    }

    /**
     * Função para criar campo em forma de moeda utilizando o separador de
     * decimal com ponto.
     *
     * @param f float numero que deseja manter as casas decimais
     * @return String
     */
    public static String formatMoney(float f) {
        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(LOCAL));
        return df.format(f);
    }

    /**
     * Função para calcular data adicionando ou removendo dias.
     *
     * @param date Date
     * @param dias int O valor adotado pode ser negativo.
     * @return Date
     */
    public static Date addDias(Date date, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dias);
        return calendar.getTime();
    }

    /**
     * Função retorna a diferença em dias enre o intervalo de data1 e data2.
     *
     * @param date1 Date
     * @param date2 Date esse parâmetro deve ser maior que o primeiro caso
     * contrário uma exceção será executada
     * @return int intervalo de dias
     * @throws java.lang.Exception
     */
    public static int diferenceDates(Date date1, Date date2) throws Exception {

        if (date1.after(date2)) {
            throw new Exception("O segundo parâmetro deve ser maior que o primeiro.");
        }

        long differenceMilliSeconds = date2.getTime() - date1.getTime();
        float r = (differenceMilliSeconds / 1000 / 60 / 60 / 24);
        return Math.round(r);

    }

    /**
     * Função para calcular data adicionando ou removendo o tempo.
     *
     * @param date Date
     * @param tempo Integer Quantidade que quer adicionar
     * @param type Integer tipo 0 hora, 1 minuto, 2 segundo. Qualquer número
     * diferente será adotado o type como minuto.
     * @return Date
     */
    public static Date addTime(Date date, int tempo, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (type) {
            case 0:
                calendar.add(Calendar.HOUR, tempo);
                break;
            case 1:
                calendar.add(Calendar.MINUTE, tempo);
                break;
            case 2:
                calendar.add(Calendar.SECOND, tempo);
                break;
            default:
                calendar.add(Calendar.MINUTE, tempo);
                break;
        }
        return calendar.getTime();
    }

    /**
     * M&eacute;todo para escrever um arquivo em algum local do
     * sistema/servidor. Testa se existe o arquivo, em caso de existir um
     * arquivo com o mesmo nome sobreescreve a informa&ccedil;&otilde;es pela
     * nova enviada.
     *
     * @param String filename Diret&oacute;rio + Nome do arquivo onde deseja
     * salvar
     * @param String content Conte&uacute;do que deseja salvar
     */
    public static void writeFile(String filename, String content) throws Exception {
        /*
         * Cria o Diretório caso necessário
         */
        String[] dir = filename.split("/");
        String diretorio = "";
        for (int i = 0; i < (dir.length - 1); i++) {
            diretorio = dir[i] + "/";
        }

        if (!diretorio.equals("")) {
            File w = new File(diretorio);
            w.mkdir();
        }

        /*
         * Cria o Arquivo
         */
        File x = new File(filename);
        if (x.exists()) {
            x.delete();
        }

        String text = content;
        byte[] bytes = text.getBytes("UTF8");
        String newText = new String(bytes);
        content = newText;

        FileWriter w = new FileWriter(filename);
        w.write(content);
        w.close();
    }

    /**
     * M&eacute;todo para escrever um arquivo em algum local do
     * sistema/servidor. Testa se existe o arquivo, em caso de existir um
     * arquivo com o mesmo nome sobreescreve a informa&ccedil;&otilde;es pela
     * nova enviada.
     *
     * @param String filename Diret&oacute;rio + Nome do arquivo onde deseja
     * salvar
     * @param String content Conte&uacute;do que deseja salvar
     * @param String charset Definie qual ser&aacute; ser&aacute; a
     * codifica&ccedil;&atilde;o do arquivo.
     */
    public static void writeFile(String filename, String content, String charset) throws Exception {
        /*
         * Cria o Diretório caso necessário
         */
        String[] dir = filename.split("/");
        String diretorio = "";
        for (int i = 0; i < (dir.length - 1); i++) {
            diretorio = dir[i] + "/";
        }

        if (!diretorio.equals("")) {
            File w = new File(diretorio);
            w.mkdir();
        }

        /*
         * Cria o Arquivo
         */
        File x = new File(filename);
        if (x.exists()) {
            x.delete();
        }

        String text = content;
        byte[] bytes = text.getBytes(charset.toUpperCase());
        String newText = new String(bytes);
        content = newText;

        FileWriter w = new FileWriter(filename);
        w.write(content);
        w.close();
    }

    /**
     * Função redimensiona a imagem em um tamanho menor.
     */
    public static void redimensionarImagem(File imagem, String formato, String novoNome, int width, int height) throws IOException {
        try {
            while (!imagem.exists()) {
                System.out.println("Esperando imagem...");
            }

            BufferedImage buffer = ImageIO.read(imagem);

            int imgW = buffer.getWidth();
            int imgH = buffer.getHeight();

            // manter configurações originais (if) ou redimensionar (else).
            if ((imgW <= width) || (imgH <= height)) {
                width = imgW;
                height = imgH;
            } else {
                double scale1 = Double.parseDouble(width + "") / Double.parseDouble(imgW + "");
                double scale2 = Double.parseDouble(height + "") / Double.parseDouble(imgH + "");
                double scale = (scale1 > scale2) ? scale2 : scale1;

                Long w = Math.round(imgW * scale);
                Long h = Math.round(imgH * scale);

                width = w.intValue();
                height = h.intValue();
            }

            Map<Key, Object> map = new HashMap<>();
            map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            map.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            map.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            BufferedImage new_img = new BufferedImage(width, height, (formato.equalsIgnoreCase("png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB));

            Graphics2D g = new_img.createGraphics();
            g.setRenderingHints(map);

            g.drawImage(buffer, 0, 0, width, height, null);
            ImageIO.write(new_img, formato, new File(novoNome));
        } catch (Exception e) {
            System.out.println("Não foi possível redimencionar o arquivo!");
        }
    }

    /**
     * Função converter numeral em segundos em data, minuto e segundo.
     *
     * @param time int
     * @return String hh:mm:ss
     */
    public static String secondsToString(int time) {
        int hours = time / 3600;
        int remainder = time % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;

        String secondsStr = (seconds < 10 ? "0" : "") + seconds;
        String minutesStr = (minutes < 10 ? "0" : "") + minutes;
        String hoursStr = (hours < 10 ? "0" : "") + hours;
        return hoursStr + ":" + minutesStr + ":" + secondsStr;
    }

    /**
     * Função embaralha o conteúdo da String. Ótimo para utilizar em recuperação
     * de senhas.
     *
     *
     * @param input String
     * @return String a String de entrada embaralhada
     */
    public static String shuffle(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }

        StringBuilder output = new StringBuilder(input.length());
        while (!characters.isEmpty()) {
            int randPicker = (int) (Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }

        return output.toString();
    }

    /**
     * Calculo do Modulo 11 para geracao do digito verificador de boletos
     * bancarios conforme documentos obtidos da Febraban - www.febraban.org.br
     *
     * @param num String String numérica para a qual se deseja calcularo digito
     * verificador;
     * @param base int Valor maximo de multiplicacao [2-base]
     * @param r int Quando especificado um devolve somente o resto
     *
     * @return int Retorna o Digito verificador.
     */
    public static int modulo11(String num, int base, int r) {
        return Util.getMod11(num, base, r);
    }

    /**
     * Calculo do Modulo 11 para geracao do digito verificador de boletos
     * bancarios conforme documentos obtidos da Febraban - www.febraban.org.br
     * Padrao para base = 9 e resto = 0
     *
     * @param num String String numérica para a qual se deseja calcularo digito
     * verificador;
     *
     * @return int Retorna o Digito verificador.
     */
    public static int modulo11(String num) {
        return Util.getMod11(num, 9, 0);
    }

    private static int getMod11(String num, int base, int r) {
        int soma = 0;
        int fator = 2;
        String[] numeros, parcial;
        numeros = new String[num.length() + 1];
        parcial = new String[num.length() + 1];

        /*
         * Separacao dos numeros
         */
        for (int i = num.length(); i > 0; i--) {
            // pega cada numero isoladamente
            numeros[i] = num.substring(i - 1, i);
            // Efetua multiplicacao do numero pelo falor
            parcial[i] = String.valueOf(Integer.parseInt(numeros[i]) * fator);
            // Soma dos digitos
            soma += Integer.parseInt(parcial[i]);
            if (fator == base) {
                // restaura fator de multiplicacao para 2
                fator = 1;
            }
            fator++;

        }

        /*
         * Calculo do modulo 11
         */
        if (r == 0) {
            soma *= 10;
            int digito = soma % 11;
            if (digito == 10) {
                digito = 0;
            }
            return digito;
        } else {
            int resto = soma % 11;
            return resto;
        }
    }

    /**
     * Calcula modulo de 10.
     *
     * @param numero String
     * @return String
     */
    public static int modulo10(String num) {

        //variáveis de instancia  
        int soma = 0;
        int resto = 0;
        int dv = 0;
        String[] numeros = new String[num.length() + 1];
        int multiplicador = 2;
        String aux;
        String aux2;
        String aux3;

        for (int i = num.length(); i > 0; i--) {
            //Multiplica da direita pra esquerda, alternando os algarismos 2 e 1  
            if (multiplicador % 2 == 0) {
                // pega cada numero isoladamente    
                numeros[i] = String.valueOf(Integer.valueOf(num.substring(i - 1, i)) * 2);
                multiplicador = 1;
            } else {
                numeros[i] = String.valueOf(Integer.valueOf(num.substring(i - 1, i)) * 1);
                multiplicador = 2;
            }
        }

        // Realiza a soma dos campos de acordo com a regra  
        for (int i = (numeros.length - 1); i > 0; i--) {
            aux = String.valueOf(Integer.valueOf(numeros[i]));

            if (aux.length() > 1) {
                aux2 = aux.substring(0, aux.length() - 1);
                aux3 = aux.substring(aux.length() - 1, aux.length());
                numeros[i] = String.valueOf(Integer.valueOf(aux2) + Integer.valueOf(aux3));
            } else {
                numeros[i] = aux;
            }
        }

        //Realiza a soma de todos os elementos do array e calcula o digito verificador  
        //na base 10 de acordo com a regra.       
        for (int i = numeros.length; i > 0; i--) {
            if (numeros[i - 1] != null) {
                soma += Integer.valueOf(numeros[i - 1]);
            }
        }
        resto = soma % 10;
        dv = 10 - resto;
        if (resto == 0) {
            dv = 0;
        }

        //retorna o digito verificador  
        return dv;

    }

    /**
     * Verifica se a String é um número. Caso exista um caracter dentro da
     * String não número será retornado false caso contrário true.
     *
     * @param s String
     * @return boolean
     */
    public static boolean isNumber(String s) {
        boolean d = true;
        if (s == null || s.trim().equals("")) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                d = false;
                break;
            }
        }
        return d;
    }

    /**
     * Função pega uma palavra e escreve ela de trás para frente.
     */
    public static String escreverDeTrasParaFrente(String s) {
        String buffer = "";
        int i = s.length() - 1;
        while (i >= 0) {
            buffer += s.charAt(i) + "";
            i--;
        }
        return buffer;
    }

    /**
     * Função remove as máscaras da String; A função considerá máscara tudo que
     * não for número.
     *
     * @param s String
     * @return String
     */
    public static String onlyNumber(String s) {
        String unmask = "";
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                if (Character.isDigit(s.charAt(i))) {
                    unmask += s.charAt(i) + "";
                }
            }
        }
        return unmask;
    }

    /**
     * Função remove todos os números da string retorando a string sem números.
     *
     * @param s String
     * @return String
     */
    public static String noDigit(String s) {
        String nodigit = "";
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                if (!Character.isDigit(s.charAt(i))) {
                    nodigit += s.charAt(i) + "";
                }
            }
        }
        return nodigit.trim();
    }

    /**
     * Função para abreviar nomes compostos, com mais de dois nomes.
     *
     * @param nome String
     * @return String
     */
    public static String abreviarNome(String nome) {
        String[] artigos = new String[]{"de", "da", "dos", "do", "das", "e"};

        String names[] = nome.split(" ");
        int tamanho = names.length;
        String abv = names[0];

        for (int i = 1; i < tamanho - 1; i++) {
            boolean acc = true;
            for (String a : artigos) {
                if (a.equals(names[i].toLowerCase())) {
                    acc = false;
                    break;
                }
            }
            if (acc) {
                if (names[i].length() > 0) {
                    abv += " " + names[i].charAt(0) + ".";
                }
            }
        }

        abv += " " + names[tamanho - 1];
        return abv;
    }

    public static String format(String pattern, Object value) {
        MaskFormatter mask;
        try {
            mask = new MaskFormatter(pattern);
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Função para realizar o upload do arquivo e retorna o endereço onde o
     * arquivo foi colocado.
     *
     * @param upload File
     * @param uploadContentType String
     * @param dir String pasta onde será salvo o arquivo
     * @param allowed String[] content-type permitidos
     * @param request HttpServletRequest necessário para obter o endereço do
     * @param originalFileName String nome original do arquivo arquivo.
     * @return String endereço do arquivo;
     * @throws mobi.stos.gorest.exception.FileTypeNotAllowedException
     * @throws Exception
     */
    public static String uploadFile(File upload, String uploadContentType,
            String dir, String[] allowed, HttpServletRequest request, String originalFileName)
            throws FileTypeNotAllowedException, Exception {
        System.out.println("Iniciando o upload do arquivo.");
        if (upload != null) {

            Util.setAllowTypes(allowed);
            String fname = originalFileName;
            String extensao = fname.substring(fname.lastIndexOf("."));

            Util.allowedTypes(uploadContentType);
            String philename = Util.newFileName(extensao);
            ServletContext servletContext = request.getServletContext();

            String filePath = servletContext.getRealPath(dir);
            if (filePath == null) {
                filePath = servletContext.getRealPath("/") + File.separator + dir;
            }

//            System.out.println(filePath);
            File fileToCreate = new File(filePath, philename);
            FileUtils.copyFile(upload, fileToCreate);

            if (uploadContentType.contains("image")) {
                redimensionarImagem(fileToCreate, extensao.substring(1), (filePath + "/" + philename), 1920, 1080);
            }
            return dir + philename;
        } else {
            throw new FileNotFoundException();
        }
    }

    /**
     * Função para realizar o upload do arquivo e retorna o endereço onde o
     * arquivo foi colocado.
     *
     * @param inputStream InputStream
     * @param contentType String
     * @param dir String pasta onde será salvo o arquivo
     * @param allowed String[] content-type permitidos
     * @param context ServletContext necessário para obter o endereço do
     * @param originalFileName String nome original do arquivo arquivo.
     * @return String endereço do arquivo;
     * @throws Exception
     */
    public static String uploadFile(InputStream inputStream, String contentType,
            String dir, String[] allowed, ServletContext context, String originalFileName) throws Exception {

        if (inputStream != null) {
            Util.setAllowTypes(allowed);
            String fname = originalFileName;
            String extensao = fname.substring(fname.lastIndexOf("."));

            Util.allowedTypes(contentType);
            String philename = Util.newFileName(extensao);

            String filePath = context.getRealPath(dir);
            if (filePath == null) {
                filePath = context.getRealPath("/") + File.separator + dir;
            }

            File fileToCreate = new File(filePath, philename);
            FileUtils.copyInputStreamToFile(inputStream, fileToCreate);

            if (contentType.contains("image")) {
                Util.redimensionarImagem(fileToCreate, extensao.substring(1), (filePath + "/" + philename), 1920, 1080);
            }
            return dir + philename;
        } else {
            throw new FileNotFoundException();
        }
    }

    /**
     * upload de imagem já criando miniatura onde primeira posição é a imagem
     * full hd e a segunda posição é a miniatura
     *
     * @param upload
     * @param uploadContentType
     * @param dir
     * @param originalFileName
     * @param allowed
     * @param request
     * @return String[]
     * @throws mobi.stos.gorest.exception.FileTypeNotAllowedException
     */
    public static String[] uploadImageWithThumb(File upload, String uploadContentType,
            String dir, String[] allowed, HttpServletRequest request, String originalFileName)
            throws FileTypeNotAllowedException, Exception {

        if (upload != null) {
            Util.setAllowTypes(allowed);
            String fname = originalFileName;
            String extensao = fname.substring(fname.lastIndexOf("."));

            Util.allowedTypes(uploadContentType);
            String philename = Util.newFileName(extensao);
            String thumbname = "thumb_" + philename;

            ServletContext servletContext = request.getServletContext();

            String filePath = servletContext.getRealPath(dir);
            if (filePath == null) {
                filePath = servletContext.getRealPath("/") + File.separator + dir;
            }

            // arquivo full hd
            File fileToCreate = new File(filePath, philename);
            FileUtils.copyFile(upload, fileToCreate);
            Util.redimensionarImagem(fileToCreate, extensao.substring(1), (filePath + "/" + philename), 1920, 1080);

            // miniatura
            fileToCreate = new File(filePath, thumbname);
            FileUtils.copyFile(upload, fileToCreate);
            Util.redimensionarImagem(fileToCreate, extensao.substring(1), (filePath + "/" + thumbname), 355, 200);

            return new String[]{
                (dir + philename),
                (dir + thumbname)
            };
        } else {
            throw new FileNotFoundException();
        }
    }

    public static String[] uploadImageWithThumb(InputStream inputStream, String contentType,
            String dir, String[] allowed, ServletContext context, String originalFileName) throws Exception {

        if (inputStream != null) {
            Util.setAllowTypes(allowed);
            String fname = originalFileName;
            String extensao = fname.substring(fname.lastIndexOf("."));

            Util.allowedTypes(contentType);

            String philename = Util.newFileName(extensao);
            String thumbname = "thumb_" + philename;

            String filePath = context.getRealPath(dir);
            if (filePath == null) {
                filePath = context.getRealPath("/") + File.separator + dir;
            }

            // resolução full hd
            File fileToCreate = new File(filePath, philename);
            FileUtils.copyInputStreamToFile(inputStream, fileToCreate);
            Util.redimensionarImagem(fileToCreate, extensao.substring(1), (filePath + "/" + philename), 1920, 1080);

            // miniatura
            File miniaturaToCreate = new File(filePath, thumbname);
            FileUtils.copyFile(fileToCreate, miniaturaToCreate);
            Util.redimensionarImagem(miniaturaToCreate, extensao.substring(1), (filePath + "/" + thumbname), 178, 100);

            return new String[]{
                (dir + philename),
                (dir + thumbname)
            };
        } else {
            throw new FileNotFoundException();
        }
    }

    public static String getRealpath(HttpServletRequest request, String file) {
        ServletContext servletContext = request.getServletContext();

        String filePath = servletContext.getRealPath(file);
        if (filePath == null) {
            filePath = servletContext.getRealPath("/") + File.separator + file;
        }
        return filePath;
    }

    public static String getRealpath(ServletContext context, String file) {
        String filePath = context.getRealPath(file);
        if (filePath == null) {
            filePath = context.getRealPath("/") + File.separator + file;
        }
        return filePath;
    }

    public static void deleteFile(String arquivo, HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
        String filePath = servletContext.getRealPath("/");
        File file = new File(filePath + arquivo);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String prepareWildcard(String str) {
        String[] blacklist = new String[]{
            "da",
            "de",
            "dos",
            "di",
            "a",
            "e",
            "i",
            "o",
            "&",
            "em",
            "com"
        };

        str = unaccent(str);
        str = str.toUpperCase() //Tudo maiusculo
                .replaceAll(" & ", " E ")
                .replaceAll("\\s*\\.\\s*", ". ") //converte abreviaçoes(.) para um padrão ". "
                .replaceAll("\\s+", " ") //apaga espacos duplicados
                .replaceAll(" M(\\.)? E(\\.)? ", " ME ") //corrige ME
                .replaceAll(" D[AEIOU](S)? ", " ") //apaga DA DE DI DO DU DAS DES DIS DOS DUS
                .replaceAll(" [AE] ", " ") //apaga A E
                .replaceAll(" AS ", " ") //apaga AS
                .replaceAll(" EM ", " ") //apaga EM
                .replaceAll("\\s?-\\s?", " ") //apaga -
                .replaceAll("[^a-zA-Z 0-9]+", "");

        String nQuery = "";
        String[] preparado = str.toLowerCase().split(" ");
        for (String s : preparado) {
            boolean denied = false;
            for (String b : blacklist) {
                if (s.trim().equals(b)) {
                    denied = true;
                    break;
                }
            }
            if (!denied) {
                if (!nQuery.equals("")) {
                    nQuery += "|";
                }
                nQuery += s.trim();
            }
        }
        return nQuery;
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive. The
     * difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static String permalink(String s) {
        s = unaccent(s);
        s = s.toLowerCase().replaceAll("\\s+", " ").replaceAll("[^a-zA-Z 0-9]+", "");
        s = s.replaceAll(" ", "-");
        return s;
    }

    public static String unaccent(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    public static boolean isEmailValid(String email) {
        if (isEmpty(email)) {
            return false;
        }
        String EMAIL_PATTERN
                = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String mesPorExtenso(int mes) {
        String[] meses = new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        return meses[mes];
    }

    public static String mesPorExtensoCurto(int mes) {
        String[] meses = new String[]{"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        return meses[mes];
    }

    public static String cutIfNecessary(String texto, int length) {
        if (texto != null && texto.length() > length) {
            return texto.substring(0, length);
        }
        return texto;
    }

    /**
     * Função retorna distância em metros a partir de uma latitude e longitude
     * de inicio e uma latitude e longitude de destino.
     *
     * @param orglat double Latitude de início
     * @param orglon double Longitude de início
     * @param destlat double Latitude de destino
     * @param destlon double Longitude de destino
     * @return double distância em metros entre as posições geográficas.
     */
    public static double haversine(double orglat, double orglon, double destlat, double destlon) {
        orglat = orglat * Math.PI / 180;
        orglon = orglon * Math.PI / 180;
        destlat = destlat * Math.PI / 180;
        destlon = destlon * Math.PI / 180;

        double raioterra = 6378140; // METROS
        double dlat = destlat - orglat;
        double dlon = destlon - orglon;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(orglat) * Math.cos(destlat) * Math.pow(Math.sin(dlon / 2), 2);
        double distancia = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return raioterra * distancia;
    }

    /***
     * Check is valid json
     * *** Wild mode ***
     * @param json String
     * @return boolean
     */
    public static boolean isJSONValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            try {
                new JSONArray(json);
            } catch (JSONException ex) {
                return false;
            }
        }
        return true;
    }
    
    public static String trim(String s) {
        if (s == null) {
            return null;
        } else {
            return s.trim();
        }
    }
    
    
     /**
     * Função checa CPF válido.
     * @param CPF String
     * @return boolean
     */
    public static boolean isCPF(String CPF) {
        CPF = onlyNumber(CPF);
        if (CPF.equals("00000000000") || CPF.equals("11111111111")
                || CPF.equals("22222222222") || CPF.equals("33333333333")
                || CPF.equals("44444444444") || CPF.equals("55555555555")
                || CPF.equals("66666666666") || CPF.equals("77777777777")
                || CPF.equals("88888888888") || CPF.equals("99999999999")
                || (CPF.length() != 11)) {
            return false;
        }
        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }
            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48);
            }
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10))) {
                return true;
            } else {
                return false;
            }
        } catch (InputMismatchException erro) {
            return false;
        }

    }
}
