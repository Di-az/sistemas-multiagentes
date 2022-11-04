from mesa import Agent, Model
from mesa.space import Grid
from mesa.time import RandomActivation
from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer

class Ghost(Agent):
    def __init__(self, model, pos):
        super().__init__(model.next_id(), model)
        self.pos = pos
    def step(self):
        pass

class Maze(Model):
    def __init__(self):
        super().__init__()
        self.schedule = RandomActivation(self)
        self.grid = Grid(17, 14, torus=False)

        ghost = Ghost(self, (8, 6))
        self.grid.place_agent(ghost, ghost.pos)
        self.schedule.add(ghost)
        
    def step(self):
        self.schedule.step()

def agent_portrayal(agent):
    return {"Shape": "circle", "r": 1, "Filled": "true", "Color": "Orange", "Layer": 0}

grid = CanvasGrid(agent_portrayal, 17, 14, 450, 450)

server = ModularServer(Maze, [grid], "PacMan", {})
server.port = 8522
server.launch()