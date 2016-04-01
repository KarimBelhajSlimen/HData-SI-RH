package cors;

import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Created by root on 23/03/16.
 */
public class CorsAction extends play.mvc.Action.Simple{

    /**
     * Adds Access-Control-Allow-Origin headers
     */
    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        ctx.response().setHeader("Access-Control-Allow-Origin", "*");
        return delegate.call(ctx);

    }
}
