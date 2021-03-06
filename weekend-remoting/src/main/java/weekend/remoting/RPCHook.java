package weekend.remoting;

import weekend.remoting.protocol.RemotingCommand;


public interface RPCHook {
    public void doBeforeRequest(final String remoteAddr, final RemotingCommand request);


    public void doAfterResponse(final RemotingCommand request, final RemotingCommand response);
}
