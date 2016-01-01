package legends.model.events;

import java.util.Optional;

import legends.model.HistoricalFigure;
import legends.model.HistoricalFigureLink;
import legends.model.World;
import legends.model.events.basic.HfEvent;

public class AddHfHfLinkEvent extends HfEvent {
	private int hfIdTarget = -1;

	public int getHfIdTarget() {
		return hfIdTarget;
	}

	public void setHfIdTarget(int hfIdTarget) {
		this.hfIdTarget = hfIdTarget;
	}

	@Override
	public boolean setProperty(String property, String value) {
		switch (property) {

		case "hfid_target":
			setHfIdTarget(Integer.parseInt(value));
			break;

		default:
			return super.setProperty(property, value);
		}
		return true;
	}

	@Override
	public boolean isRelatedToHf(int hfId) {
		return super.isRelatedToHf(hfId) || hfIdTarget == hfId;
	}

	@Override
	public String getShortDescription() {
		HistoricalFigure hf = World.getHistoricalFigure(getHfId());
		HistoricalFigure target = World.getHistoricalFigure(getHfIdTarget());
		Optional<HistoricalFigureLink> link = hf.getHistoricalFigureLinks().stream()
				.filter(HistoricalFigureLink::isNotFamily).filter(l -> l.getHistoricalFigureId() == hfIdTarget)
				.findFirst();
		if (link.isPresent()) {
			switch (link.get().getLinkType()) {
			case "deity":
				return hf.getLink() + " began worshipping " + target.getLink();
			case "spouse":
				return hf.getLink() + " married " + target.getLink();
			case "apprentice":
			case "former apprentice":
				return hf.getLink() + " became the master of " + target.getLink();
			case "master":
			case "former master":
				return hf.getLink() + " began an apprenticeship under " + target.getLink();
			case "prisoner":
				return hf.getLink() + " imprisoned " + target.getLink();
			default:
				return hf.getLink() + " link " + link.get().getLinkType() + " " + target.getLink();
			}
		}
		return hf.getLink() + " imprisoned " + target.getLink();
	}

}
