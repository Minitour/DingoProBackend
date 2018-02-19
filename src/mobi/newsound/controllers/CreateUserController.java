package mobi.newsound.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataStore;
import mobi.newsound.model.Account;
import mobi.newsound.model.OperationalOfficer;
import mobi.newsound.utils.EmailValidator;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Antonio Zaitoun on 15/02/2018.
 */
public class CreateUserController implements RESTRoute {

    private static final Gson gson = new Gson();

    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {
        try{
            AuthContext context = extractFromBody(body);

            JsonObject acccountJson = body.get("account").getAsJsonObject();

            int roleId = acccountJson.getAsJsonObject().get("ROLE_ID").getAsInt();
            String password = acccountJson.getAsJsonObject().get("password").getAsString();
            String email = acccountJson.get("EMAIL").getAsString();

            if(!EmailValidator.validate(email))
                throw new IllegalArgumentException("Invalid Email");

            Account account;

            switch (roleId){
                case 0:
                case 1:
                case 3:
                    account = gson.fromJson(acccountJson,Account.class);
                    break;
                case 2:
                    account = gson.fromJson(acccountJson,OperationalOfficer.class);
                    break;
                default:
                    account = null;
                    break;
            }

            assert account != null;

            account.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));

            try(DataStore db = DataStore.getInstance()){
                assert db != null;

                db.createUser(context,account);

                account.setPassword(password);
                sendEmailToUser(account);

                return JSONResponse
                        .SUCCESS();

            }catch (Exception e){
                return JSONResponse
                        .FAILURE()
                        .message("Error: " + e.getMessage());
            }
        }catch (Exception e){
            return JSONResponse
                    .FAILURE()
                    .message(e.getMessage());
        }

    }

    private void sendEmailToUser(Account account){
        System.err.println("Sending Email Is not supported in this system.");
//        String email = account.getEMAIL();
//        String title = "Account Credentials";
//        String message = "Your Dingo VRS account is ready!\n\n" +
//                "Your Login Info:\n" +
//                "email: "+account.getEMAIL()+"\n" +
//                "password: "+account.getPassword();
//
//        try {
//            MailServiceController.sendMail(email,title,message);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }
}
