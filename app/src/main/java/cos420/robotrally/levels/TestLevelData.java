package cos420.robotrally.levels;

import java.util.ArrayList;
import java.util.List;

import cos420.robotrally.enumerations.ObstacleType;
import cos420.robotrally.enumerations.SpecialCommandType;
import cos420.robotrally.helpers.GameBoardData;
import cos420.robotrally.helpers.SpecialCommandData;
import cos420.robotrally.models.Collectable;
import cos420.robotrally.models.Obstacle;

public class TestLevelData extends LevelDataTemplate {
    public TestLevelData() {
        gameBoardData = createGameBoardData();
        specialCommandAData = createSpecialCommandAData();
        specialCommandBData = createSpecialCommandBData();
    }

    @Override
    protected GameBoardData createGameBoardData() {
        List<Obstacle> obstacles = createObstacles();
        List<Collectable> collectables = createCollectables();
        return new GameBoardData(5, 0, 0, 5, 5, obstacles, collectables);
    }

    @Override
    protected SpecialCommandData createSpecialCommandAData() {
        SpecialCommandType type = SpecialCommandType.REPEAT;
        Object[] params = {2, 1};
        return new SpecialCommandData(type, params);
    }

    @Override
    protected SpecialCommandData createSpecialCommandBData() {
        SpecialCommandType type = SpecialCommandType.REPEAT;
        Object[] params = {1, 2};
        return new SpecialCommandData(type, params);
    }

    @Override
    protected List<Obstacle> createObstacles() {
        List<Obstacle> list = new ArrayList<>();
        list.add(new Obstacle(ObstacleType.WALL, 0, 2));
        list.add(new Obstacle(ObstacleType.WALL, 1, 2));
        list.add(new Obstacle(ObstacleType.CHAIR, 2, 0));
        list.add(new Obstacle(ObstacleType.CHAIR, 3, 3));
        return list;
    }

    @Override
    protected List<Collectable> createCollectables() {
        List<Collectable> list = new ArrayList<>();
        list.add(new Collectable(3, 1));
        list.add(new Collectable(1, 3));
        list.add(new Collectable(3, 4));
        return list;
    }
}
