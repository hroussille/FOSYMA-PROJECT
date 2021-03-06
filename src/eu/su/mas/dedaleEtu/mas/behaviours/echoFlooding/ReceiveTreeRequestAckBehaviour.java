package eu.su.mas.dedaleEtu.mas.behaviours.echoFlooding;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.AbstractMultiAgent;
import eu.su.mas.dedaleEtu.mas.agents.ExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploMultiFSMBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.FSMCodes;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiveTreeRequestAckBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 7295849474602290463L;
	private boolean received = false;
	private AbstractMultiAgent _myAgent;
		
	public ReceiveTreeRequestAckBehaviour(AbstractMultiAgent myagent) {
		super(myagent);
		this._myAgent = myagent;
	}
	
	@Override
	public void action() {
		this.received = false;
		MessageTemplate pattern = MessageTemplate.and(MessageTemplate.MatchProtocol("TREE-REQUEST-ACK"), MessageTemplate.MatchPerformative(ACLMessage.AGREE));
		pattern = MessageTemplate.and(pattern, MessageTemplate.not(MessageTemplate.MatchSender(this.myAgent.getAID())));
		
		ACLMessage msg;
		
		msg = ((AbstractDedaleAgent)this.myAgent).receive(pattern);
		
		if (msg != null) {
			this.received = true;
			System.out.println(this.myAgent.getLocalName().toString() + " Received Tree Request ACK from " + msg.getSender().toString());
			
			if (this._myAgent.treeExist(msg.getContent())) {
				System.out.println(this.myAgent.getLocalName().toString() + " Adding " + msg.getSender().toString() + " to tree");
				this._myAgent.addChildToTree(msg.getContent(), msg.getSender());
				System.out.println(this._myAgent.getTree(msg.getContent()) + " Child count : " + this._myAgent.getTree(msg.getContent()).getChildCount());
			}
		}
	}
		
	public int onEnd() {
		if (this.received)
			return FSMCodes.Events.SUCESS.ordinal();
		return FSMCodes.Events.FAILURE.ordinal();
	}
}
