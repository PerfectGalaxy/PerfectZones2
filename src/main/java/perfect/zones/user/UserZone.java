package perfect.zones.user;

import org.bukkit.entity.Player;
import perfect.zones.PerfectZones;
import perfect.zones.managers.zone.Zone;

import java.util.ArrayList;
import java.util.List;

public class UserZone implements PUserZone{

    private Zone zone;
    private final Player player;
    private List<Zone> foundedZones;
    private String zoneName;

    public UserZone(Player player) {
        this.zone = null;
        this.player = player;
        this.zoneName = PerfectZones.getPerfectZones().getAllFiles().getConfig().parseColor(PerfectZones.getPerfectZones().getAllFiles().getConfig().getString("Zone.Default_Zone"));
        this.foundedZones = new ArrayList<>();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Zone getZone() {
        return this.zone;
    }

    @Override
    public void setZone(Zone zone) {
        this.zone = zone;
    }

    @Override
    public String getZoneName() {
        return this.zoneName;
    }

    @Override
    public void setZoneName(String zone) {
        this.zoneName = zone;
    }

    @Override
    public List<Zone> getFoundedZones() {
        return this.foundedZones;
    }

    @Override
    public void setFoundedZones(List<Zone> foundedZones) {
        this.foundedZones = foundedZones;
    }

    @Override
    public boolean isFoundZone(Zone zone) {
        return this.foundedZones.contains(zone);
    }

    @Override
    public void addFoundZone(Zone zone) {
        if(isFoundZone(zone)) return;
        this.foundedZones.add(zone);
    }

    @Override
    public void removeFoundZone(Zone zone) {
        if(!isFoundZone(zone)) return;
        this.foundedZones.remove(zone);
    }
}