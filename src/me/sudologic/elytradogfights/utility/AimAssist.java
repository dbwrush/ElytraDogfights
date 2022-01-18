package me.sudologic.elytradogfights.utility;

import me.sudologic.elytradogfights.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class AimAssist implements Listener {
    static Main main = Main.plugin;

    @EventHandler
    public void playerShoot(EntityShootBowEvent e) {
        Entity arrow = e.getProjectile();
        if(e.getEntity().getWorld().equals(main.world) == false) {
            return;
        }
        if(main.useAimAssist = false) {
            return;
        }
        if(e.getEntity() instanceof Player == false || main.gameIsRunning == false) {
            return;
        }
        if(!arrow.getClass().isAssignableFrom(Arrow.class)) {
            return;
        }
        Player shooter = (Player) e.getEntity();
        adjustArrow((Arrow) arrow, shooter);
        e.setCancelled(true);
    }
    public static void adjustArrow(Arrow arrow, Player shooter) {
        boolean isTeams = main.TeamsMode;
        List<Player> possibleTargets = new ArrayList<Player>();
        Vector rawAim = shooter.getEyeLocation().getDirection().normalize();
        if(isTeams) {
            if(main.redInGame.contains(shooter)) {
                possibleTargets = main.blueInGame;
            } else {
                possibleTargets = main.redInGame;
            }
        } else {
            for(Player p : main.inGame) {
                if(p.equals(shooter) == false) {
                    possibleTargets.add(p);
                }
            }
        }
        ArrayList<Vector> possibleVectors = new ArrayList<Vector>();
        for(Player p : possibleTargets) {
            possibleVectors.add(p.getLocation().toVector().subtract(shooter.getLocation().toVector()).normalize());
        }
        Vector toTarget = possibleVectors.get(0);
        double toTargetDot = toTarget.dot(rawAim);
        for(Vector v : possibleVectors) {
            if(v.dot(rawAim) < toTargetDot) {
                toTarget = v;
                toTargetDot = v.dot(rawAim);
            }
        }
        int tolerance = main.aimAssistTolerance;
        double angle = Math.acos(toTargetDot);
        angle *= 180/Math.PI;
        Vector finalVelocity = rawAim;
        if(angle < tolerance || angle > 360 - tolerance) {
            finalVelocity = toTarget.multiply(main.aimAssistStrength).add(rawAim.multiply(1 - main.aimAssistStrength));
        }
        finalVelocity = finalVelocity.multiply(main.autoFireArrowSpeed);
        finalVelocity.add(shooter.getVelocity());
        arrow.setVelocity(finalVelocity);
    }
}
