
---------------------XML配置略---------------------------------
web.xml
---------------------Filter主要代码----------------------------
DecryptedParameterRequest wrappedRequest;
EncryptedParameterResponse wrappedResponse;
try {
	wrappedRequest = new DecryptedParameterRequest((HttpServletRequest)request, dKey);
} catch (Exception e) {
	log.error("解密失败", e);
	return;
}
try {
	wrappedResponse = new EncryptedParameterResponse((HttpServletResponse) response);
} catch (Exception e) {
	log.error("加密失败", e);
	return;
}
filterChain.doFilter(wrappedRequest != null ? wrappedRequest : request, wrappedResponse != null? wrappedResponse :response);

try {
    //取返回的json串
	byte[] responseBody = wrappedResponse.getBufferedBytes();
    //加密
	String encryptStr = *****Util.encrypt(new String(responseBody, "utf-8"), dKey);
	response.setContentLength(encryptStr.length());
	response.getOutputStream().write(encryptStr.getBytes());
} catch (Exception e) {
	log.error(this.getClass().getName() + "doFilter", e);
} finally {
	response.getOutputStream().flush();
	response.getOutputStream().close();
}

---------------------DecryptedParameterRequest解密包装主要代码----------------------------

public class DecryptedParameterRequest extends HttpServletRequestWrapper {

    private final byte[] decryptedBytes;

    public DecryptedParameterRequest(HttpServletRequest request, String encryptedDesKey) throws Exception {
        super(request);
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        String encryptedData = new String(body, Charset.forName("UTF-8"));
        try {
            decryptedBytes = *****Util.decrypt(encryptedData, encryptedDesKey).getBytes();
        } catch (Exception e) {
            throw e;
        }
    }


    //重写入流，不然流在上个步骤被使用了，后面获取的body为空
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), "UTF-8"));
    }

    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(decryptedBytes);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }
}

---------------------EncryptedParameterResponse加密包装主要代码----------------------------

public class EncryptedParameterResponse extends HttpServletResponseWrapper {

    //截获输出流，放入baos
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private PrintWriter pw = null;


    public EncryptedParameterResponse(HttpServletResponse response) throws Exception {
        super(response);
    }

    //返回包装了baos的ServletOutputStream对象，用户每次的输出都会被“截获”到baos中
    public ServletOutputStream getOutputStream() throws IOException {
        return new MyServletOutputStream(baos);
    }

    //截获字符流：放到baos中
    public PrintWriter getWriter() throws IOException {
        pw = new PrintWriter(new OutputStreamWriter(baos, super.getCharacterEncoding()));
        return pw;
    }

    //获取截获的字节数组
    public byte[] getBufferedBytes() {
        try {
            if (pw != null) {
                pw.close();
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}

//改写ServletOutputStream --> 包装流
class MyServletOutputStream extends ServletOutputStream {

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public MyServletOutputStream(ByteArrayOutputStream baos) {
        this.baos = baos;
    }

    public void write(int b) throws IOException {
        baos.write(b);
    }
}

-------------------------------------遇到的问题---------------------------------------------------

1.解密时获取的inputStream导致传给controller的body为空，实例化private final byte[] decryptedBytes;重写父类getReader()和getInputStream()，我采用的是传入key
来解密流

2.加密时获取到HttpServletResponse的返回输出流  response.getOutputStream().write(encryptStr.getBytes()); 使用的还是加密前的字符长度，加密后字符长度肯定会变长，
我遇到的就是app获取到的是加密后原始长度的被截取过了的字符串，所以需要response.setContentLength(encryptStr.length());加密后的长度即可。

3.加密解密工具类最好使用同一个，不然调试起来搞死人。



