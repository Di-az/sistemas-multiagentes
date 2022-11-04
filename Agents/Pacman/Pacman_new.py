from mesa import Agent, Model
from mesa.space import Grid
from mesa.time import RandomActivation
from mesa.visualization.modules import CanvasGrid
from mesa.visualization.ModularVisualization import ModularServer

class Ghost(Agent):
    def __init__(self, model, pos, matrix):
        super().__init__(model.next_id(), model)
        self.pos = pos
        self.matrix = matrix

    def step(self):
        next_moves = self.model.grid.get_neighborhood(self.pos, moore=False)
        next_moves_copy = [] # array to save the possible moves
        for (x,y) in next_moves:
            if self.matrix[y][x] == 1:
                next_moves_copy.append((x,y))
                
        next_move = self.random.choice(next_moves_copy)
        self.model.grid.move_agent(self, next_move)

class WallBlock(Agent):
    def __init__(self, model, pos):
        super().__init__(model.next_id(), model)
        self.pos = pos

class Maze(Model):
    def __init__(self):
        super().__init__()
        self.schedule = RandomActivation(self)
        self.grid = Grid(17, 14, torus=False)

        matrix = [
        [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0],
        [0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0],
        [0,1,0,1,0,0,0,1,1,1,0,1,0,1,0,1,0],
        [0,1,1,1,0,1,0,0,0,0,0,1,0,1,1,1,0],
        [0,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0],
        [0,1,0,1,0,1,0,0,0,0,0,1,1,1,0,1,0],
        [0,1,1,1,0,1,0,1,1,1,0,1,0,1,0,1,0],
        [0,1,0,1,0,1,0,1,1,1,0,1,0,1,0,1,0],
        [0,1,0,1,1,1,0,0,1,0,0,1,0,1,1,1,0],
        [0,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1,0],
        [0,1,1,1,0,1,0,0,0,0,0,1,0,1,1,1,0],
        [0,1,0,1,0,1,0,1,1,1,0,0,0,1,0,1,0],
        [0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,0],
        [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
        ]


        ghost = Ghost(self, (1, 1), matrix)
        self.grid.place_agent(ghost, ghost.pos)
        self.schedule.add(ghost)

        for _,x,y in self.grid.coord_iter():
            if matrix[y][x] == 0:
                # for each obstacle, create a WallBlock agent
                wallB = WallBlock(self, (x,y))
                self.grid.place_agent(wallB, wallB.pos) # ponerlo en el grid
        
    def step(self):
        self.schedule.step()

def agent_portrayal(agent):
    # render a different object from each agent
    if isinstance(agent, Ghost):
        return {"Shape": "ghost2.png", "Layer": 0}
    if isinstance(agent, WallBlock):
        return {"Shape": "rect", "w": 1, "h": 1, "Filled": "true", "Color": "Gray", "Layer": 1}

grid = CanvasGrid(agent_portrayal, 17, 14, 450, 450)

server = ModularServer(Maze, [grid], "PacMan", {})
server.port = 8522
server.launch()